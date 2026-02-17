<article class="container vstack gap-3">
  <!-- Action Menu -->
  <PageActions>
    <div slot="left">
    </div>
    <div slot="right">
      <button type="button" class="btn btn-secondary" on:click={() => openModal()}>
        <i class="fas fa-plus"></i>
        <span class="d-lg-inline d-none ms-2"> {$_('staff.add-new')}</span>
      </button>
    </div>
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
        loading={false} 
        on:edit={(e) => openModal(e.detail)} 
        on:delete={(e) => openDeleteModal(e.detail)}
        on:refresh={refreshData} 
      />
    {/if}
  </div>

  {#if showModal}
    <StaffMemberModal 
      member={selectedStaff} 
      on:close={() => showModal = false} 
      on:success={() => { showModal = false; refreshData(); }} 
    />
  {/if}

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
        request: event
      });
      return {
        data: {
          staff: res.staff || []
        }
      };
    } catch (e) {
      console.error('Failed to load staff', e);
      return {
        data: {
          staff: []
        }
      };
    }
  }
</script>

<script>
  import { base, goto } from '@panomc/sdk/svelte';
  import { _ } from '../main';
  import { PageActions, CardHeader, NoContent } from '@panomc/sdk/components/panel';
  import StaffTable from './components/StaffTable.svelte';
  import StaffMemberModal from './modals/StaffMemberModal.svelte';
  import ConfirmDeleteStaffModal, {
    show as showDeleteStaffModal,
    setCallback as setDeleteStaffModalCallback,
  } from './modals/ConfirmDeleteStaffModal.svelte';

  export let data;

  let showModal = false;
  let selectedStaff = null;

  async function refreshData() {
    await goto(`${base}/staff-management`, { invalidateAll: true });
  }

  function openModal(member = null) {
    selectedStaff = member;
    showModal = true;
  }

  function openDeleteModal(member) {
    showDeleteStaffModal(member);
  }

  setDeleteStaffModalCallback(() => {
    refreshData();
  });
</script>
