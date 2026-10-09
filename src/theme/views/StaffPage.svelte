<div class="staff-page-staff-page">
  <StaffList {staff} viewMode={config.viewMode} />
</div>

<script context="module">
    import { api } from '@panomc/sdk/plugin-api';

    const pageTitle = {
    title: 'plugins.pano-plugin-staff-page.pages.staff.title',
    subtitle: 'plugins.pano-plugin-staff-page.pages.staff.subtitle',
  };

  export async function load(event) {
    try {
      const [staffRes, configRes] = await Promise.all([
        api.get({ path: '/staffs', request: event }),
        api.get({ path: '/staff/config', request: event }),
      ]);

      return {
        data: {
          staff: staffRes.items || [],
          config: configRes || {},
        },
        pageTitle,
      };
    } catch (e) {
      console.error('Failed to load staff page data', e);
      return {
        data: {
          staff: [],
          config: {},
        },
        pageTitle,
      };
    }
  }
</script>

<script>
  import StaffList from './StaffList.svelte';

  export let data;
  $: ({ staff, config } = data);
</script>
