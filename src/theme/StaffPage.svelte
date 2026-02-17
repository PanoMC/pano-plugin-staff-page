<div class="staff-page py-5">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">

        <div class="text-center mb-5">
          <h1 class="display-4 fw-bold">{$_('pages.staff.title')}</h1>
          <p class="lead text-muted">The dedicated team behind our community.</p>
        </div>

        <StaffList {staff} viewMode={config.viewMode} />
      </div>
    </div>
  </div>
</div>

<script context="module">
  import ApiUtil from '@panomc/sdk/utils/api';

  export async function load(event) {
    try {
      const [staffRes, configRes] = await Promise.all([
        ApiUtil.get({ path: '/api/staff', request: event }),
        ApiUtil.get({ path: '/api/staff/config', request: event })
      ]);

      return {
        data: {
          staff: staffRes.staff || [],
          config: configRes || {}
        }
      };
    } catch (e) {
      console.error('Failed to load staff page data', e);
      return {
        data: {
          staff: [],
          config: {}
        }
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

<style>
  .staff-page {
    background: var(--bs-body-bg);
    min-height: 50vh;
  }
</style>
