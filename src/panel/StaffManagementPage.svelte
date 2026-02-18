<article class="container vstack gap-3">
  <!-- Action Menu -->
  <PageActions leftClasses="d-lg-flex d-none" middleClasses="d-lg-flex d-none">
    <button slot="right" type="button" class="btn btn-secondary" on:click={() => openModal()}>
      <i class="fas fa-plus"></i>
      <span class="d-lg-inline d-none ms-2"> {$_('staff.add-new')}</span>
    </button>
  </PageActions>

  <div class="card">
    <CardHeader>
      <div slot="left">
        {$_('pages.staff-management.count', { values: { count: data.staff?.length || 0 } })}
      </div>
    </CardHeader>

    {#if !data.staff || data.staff.length === 0}
      <NoContent />
    {:else}
      <StaffTable
        staff={data.staff || []}
        {loading}
        on:edit={(e) => openModal(e.detail)}
        on:delete={(e) => openDeleteModal(e.detail)}
        on:refresh={refreshData} />
    {/if}
  </div>

  <svelte:component this={AddEditStaffModal} />
  <svelte:component this={ConfirmDeleteStaffModal} />
</article>

<script context="module">
  import ApiUtil from '@panomc/sdk/utils/api';

  /**
   * @type {import("@sveltejs/kit").PageLoad}
   */
  export async function load(event) {
    const { parent } = event;
    const { pageTitle } = await parent();

    pageTitle.set('plugins.pano-plugin-staff-page.pages.staff-management.title');

    try {
      const res = await ApiUtil.get({
        path: '/api/panel/staff',
        request: event,
      });
      return {
        data: {
          staff: (res.staff || []).sort((a, b) => a.id - b.id),
        },
      };
    } catch (e) {
      console.error('Failed to load staff', e);
      return {
        data: {
          staff: [],
        },
      };
    }
  }
</script>

<script>
  import { base, goto } from '@panomc/sdk/svelte';
  import { _ } from '../main';
  import { PageActions, CardHeader, NoContent } from '@panomc/sdk/components/panel';
  import StaffTable from './components/StaffTable.svelte';
  import AddEditStaffModal, {
    show as showAddEditStaffModal,
    setCallback as setAddEditStaffModalCallback,
  } from './modals/AddEditStaffModal.svelte';
  import ConfirmDeleteStaffModal, {
    show as showDeleteStaffModal,
    setCallback as setDeleteStaffModalCallback,
  } from './modals/ConfirmDeleteStaffModal.svelte';

  export let data;

  let loading = false;

  async function refreshData() {
    loading = true;
    try {
      const res = await ApiUtil.get({
        path: '/api/panel/staff',
      });
      data.staff = (res.staff || []).sort((a, b) => a.id - b.id);
    } catch (e) {
      console.error('Failed to refresh data', e);
      // Fallback to routing refresh if manual fetch fails
      await goto(`${base}/staff-management`, { invalidateAll: true });
    } finally {
      loading = false;
    }
  }

  function openModal(member = null) {
    showAddEditStaffModal(member ? 'edit' : 'create', member);
  }

  function openDeleteModal(member) {
    showDeleteStaffModal(member);
  }

  setAddEditStaffModalCallback(() => {
    refreshData();
  });

  setDeleteStaffModalCallback(() => {
    refreshData();
  });
</script>
