<div class="container vstack gap-3">
  <PageTitle title={$_('pages.staff.title')} subtitle={$_('pages.staff.subtitle')} />

  <div class="staff-content">
    <StaffList {staff} viewMode={config.viewMode} />
  </div>
</div>

<script context="module">
  import ApiUtil from '@panomc/sdk/utils/api';

  export async function load(event) {
    try {
      const [staffRes, configRes] = await Promise.all([
        ApiUtil.get({ path: '/api/staff', request: event }),
        ApiUtil.get({ path: '/api/staff/config', request: event }),
      ]);

      return {
        data: {
          staff: staffRes.staff || [],
          config: configRes || {},
        },
      };
    } catch (e) {
      console.error('Failed to load staff page data', e);
      return {
        data: {
          staff: [],
          config: {},
        },
      };
    }
  }
</script>

<script>
  import { _ } from '../main';
  import StaffList from './components/StaffList.svelte';
  import { PageTitle } from '@panomc/sdk/components/theme';

  export let data;
  $: ({ staff, config } = data);
</script>
