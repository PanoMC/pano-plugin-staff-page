<script>
  import StaffList from './StaffList.svelte';
  import { _ } from '../../main';

  export let data = {}; // Received from ViewComponent data prop
  
  $: staffData = data?.staffData || {};
  $: staff = staffData.staff || [];
  $: config = staffData.config || {};
</script>

{#if config && config.displayLocation === 'SUPPORT_PAGE' && staff.length > 0}
  <div class="staff-integration mt-5 pt-5 border-top">
    <div class="container">
      <div class="text-center mb-5">
        <h2 class="fw-bold">Our Support Team</h2>
        <p class="text-muted">Feel free to reach out to our staff members.</p>
      </div>
      <StaffList {staff} viewMode="CARD" />
    </div>
  </div>
{/if}
