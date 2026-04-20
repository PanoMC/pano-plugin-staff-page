<div class="staff-content">
  <StaffList {staff} viewMode={config.viewMode} />
</div>

<script context="module">
    import ApiUtil from '@panomc/sdk/utils/api';

    const pageTitle = {
    title: 'plugins.pano-plugin-staff-page.pages.staff.title',
    subtitle: 'plugins.pano-plugin-staff-page.pages.staff.subtitle',
  };

  export async function load(event) {
    try {
      const [staffRes, configRes] = await Promise.all([
        ApiUtil.get({ path: '/api/staffs', request: event }),
        ApiUtil.get({ path: '/api/staff/config', request: event }),
      ]);

      return {
        data: {
          staff: staffRes.staff || [],
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
  import { _ } from '../main';
  import StaffList from './components/StaffList.svelte';

  export let data;
  $: ({ staff, config } = data);
</script>
