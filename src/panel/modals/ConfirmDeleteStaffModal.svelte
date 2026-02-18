<!-- Confirm Delete Staff Modal -->
<div aria-hidden="true" class="modal fade" bind:this={$modalElement} role="dialog" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered" role="dialog">
    <div class="modal-content">
      <div class="modal-body text-center">
        <div class="pb-3">
          <i class="fas fa-question-circle fa-3x d-block m-auto text-gray border-0 shadow-none"></i>
        </div>
        {$_('staff.confirm-delete')}
      </div>
      <div class="modal-footer flex-nowrap">
        <button
          class="btn btn-link col-6 m-0 text-decoration-none"
          type="button"
          class:disabled={loading}
          on:click={hide}>
          {$_('common.cancel') || 'Cancel'}
        </button>
        <button
          class="btn btn-danger col-6 m-0"
          type="button"
          class:disabled={loading}
          on:click={onYesClick}>
          {#if loading}
            <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"
            ></span>
          {/if}
          {$_('common.delete') || 'Delete'}
        </button>
      </div>
    </div>
  </div>
</div>

<script context="module">
  import { get, writable } from 'svelte/store';
  import ApiUtil from '@panomc/sdk/utils/api';

  const modalElement = writable();
  const member = writable({});

  let callback = (member) => {};
  let modal;

  export function show(newMember) {
    member.set(newMember);

    modal = new window.bootstrap.Modal(get(modalElement), {
      backdrop: 'static',
      keyboard: false,
    });
    modal.show();
  }

  export function setCallback(newCallback) {
    callback = newCallback;
  }

  export function hide() {
    modal.hide();
  }
</script>

<script>
  import { showToast } from '@panomc/sdk/toasts';
  import { _ } from '../../main';
  let loading = false;

  async function onYesClick() {
    if (loading) return;
    loading = true;

    try {
      const targetMember = get(member);
      const result = await ApiUtil.delete({
        path: `/api/panel/staff/${targetMember.id}`,
      });

      if (result.error) {
        console.error('Delete failed:', result.error);
        showToast('Failed to delete staff member');
      } else {
        showToast($_('toasts.delete-success'));
        callback(targetMember);
        hide();
      }
    } catch (e) {
      console.error(e);
      showToast('Error occurred while deleting staff');
    } finally {
      loading = false;
    }
  }
</script>
