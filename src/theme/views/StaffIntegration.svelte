{#if config && config.displayLocation === 'SUPPORT_PAGE' && staff.length > 0}
  <div class="staff-page-staff-integration mt-5 pt-5 border-top">
    <div class="container">
      <div class="text-center mb-5">
        <h2 class="staff-page-staff-integration__title fw-bold">{$_('pages.support.title')}</h2>
        <p>{$_('pages.support.subtitle')}</p>
      </div>
      <StaffList {staff} viewMode="CARD" />
    </div>
  </div>
{/if}

<script>
  import { derived } from 'svelte/store';
  import { _ as i18n } from '@panomc/sdk/utils/language';
  import StaffList from './StaffList.svelte';

  // plugin translations: $_('key') reads plugins.pano-plugin-staff-page.key
  const _ = derived(i18n, ($_fn) => (key, options) => $_fn(`plugins.pano-plugin-staff-page.${key}`, options));

  export let data = {}; // Received from ViewComponent data prop

  $: staffData = data?.staffData || {};
  $: staff = staffData.staff || [];
  $: config = staffData.config || {};
</script>
