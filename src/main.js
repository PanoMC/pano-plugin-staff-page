import { PanoPlugin, viewComponent } from '@panomc/sdk';
import { derived } from 'svelte/store';
import { _ as i18n } from '@panomc/sdk/utils/language';
import ApiUtil from '@panomc/sdk/utils/api';
import { showToast } from '@panomc/sdk/toasts';

const pluginId = 'pano-plugin-staff-page';

export const _ = derived(i18n, ($_fn) => {
  return (key, options) => $_fn(`plugins.${pluginId}.${key}`, options);
});

// Success/failure colouring for this plugin's toasts, matching the panel. showToast from
// @panomc/sdk/toasts is the host panel's ToastContainer `show`, whose signature is
// (text, params, toastComponent, options): passing undefined for toastComponent keeps the
// host's DefaultToast, and options.variant maps to Bootstrap's text-success / text-danger.
// These live here rather than in @panomc/sdk/toasts because this plugin is pinned to
// @panomc/sdk 1.0.0-dev.39, which predates the variants; they can be dropped for a direct
// SDK import once that pin moves. On an older panel build the extra argument is ignored and
// the toast renders neutral, so this degrades instead of breaking.
export function showSuccessToast(text, params = {}) {
  return showToast(text, params, undefined, { variant: 'success' });
}

export function showErrorToast(text, params = {}) {
  return showToast(text, params, undefined, { variant: 'danger' });
}

export default class PanoPluginStaffPage extends PanoPlugin {
  onLoad() {
    const pano = this.pano;

    if (pano.isPanel) {
      // Register Panel Page
      pano.ui.page.register({
        path: '/staff-management',
        component: viewComponent(() => import('./panel/StaffManagementPage.svelte')),
        permission: `pano.plugin.${pluginId}.manage.staff`,
      });

      // Add Panel Sidebar Link
      pano.ui.nav.site.editNavLinks((items) => {
        const playersIndex = items.findIndex((item) => item.href === '/players');
        const staffsLink = {
          href: '/staff-management',
          text: `plugins.${pluginId}.pages.staff-management.title`, // Still using the same key, will update translation
          icon: 'fas fa-users-cog',
          permission: `pano.plugin.${pluginId}.manage.staff`,
        };

        if (playersIndex !== -1) {
          items.splice(playersIndex + 1, 0, staffsLink);
        } else {
          items.push(staffsLink);
        }
        return items;
      });

      // Register Settings Hook for Addon Detail Page
      pano.ui.hook.register({
        name: `panel:plugin-detail:content:${pluginId}`,
        component: viewComponent(() => import('./panel/components/StaffSettings.svelte')),
        permission: `pano.plugin.${pluginId}.manage.staff.settings`,
      });

      // Load config when addon detail is loaded
      pano.ui.addon.onLoad(async (data, event) => {
        if (data.addon.id !== pluginId) return;

        try {
          const config = await ApiUtil.get({
            path: '/api/panel/staff/config',
            request: event,
          });
          data.addon.config = config;
        } catch (e) {
          console.error(`[${pluginId}] Failed to load config`, e);
        }
      });

    } else {
      // Theme Side
      const staffPageComponent = viewComponent(() => import('./theme/StaffPage.svelte'));

      pano.ui.app.onLoad(async (data, event) => {
        try {
          const config = await ApiUtil.get({
            path: '/api/staff/config',
            request: event,
          });

          if (config && config.displayLocation === 'THEME_PAGE' && config.pageUrl) {
            const pageUrl = config.pageUrl.startsWith('/') ? config.pageUrl : `/${config.pageUrl}`;

            // Register dynamic route
            pano.ui.page.register({
              path: pageUrl,
              component: staffPageComponent,
            });

            // Add to Theme Nav
            if (pano.ui.nav.site.editNavLinks) {
              pano.ui.nav.site.editNavLinks((navItems) => {
                if (!navItems.find((n) => n.href === pageUrl)) {
                  navItems.push({
                    href: pageUrl,
                    text: `plugins.${pluginId}.pages.staff.title`
                  });
                }
                return navItems;
              });
            }
          }

          if (config && config.displayLocation === 'SUPPORT_PAGE') {
            pano.ui.support.onLoad(async (data, event) => {
              try {
                const staffRes = await ApiUtil.get({
                  path: '/api/staffs',
                  request: event,
                });

                data.staffData = {
                  staff: staffRes.staff || [],
                  config
                };

                pano.ui.view.register({
                  viewId: 'support-content',
                  id: `${pluginId}:support-staff`,
                  component: viewComponent(() => import('./theme/components/StaffIntegration.svelte')),
                  priority: -100,
                });
              } catch (e) {
                console.error(`[${pluginId}] Failed to load staff for support lifecycle`, e);
              }
            });
          }

        } catch (e) {
          console.error(`[${pluginId}] Failed to fetch config for Theme`, e);
        }
      });
    }
  }

  onUnload() { }
}
