<script>
  import { createEventDispatcher } from 'svelte';
  import { _ } from '../../main';
  import ApiUtil from '@panomc/sdk/utils/api';
  import { showToast } from '@panomc/sdk/toasts';
  import StaffRow from './StaffRow.svelte';

  export let staff = [];
  export let loading = false;

  const dispatch = createEventDispatcher();

  function handleEdit(member) {
    dispatch('edit', member);
  }

  function handleDelete(member) {
    dispatch('delete', member);
  }
</script>

<div class="table-responsive">
  <table class="table table-hover">
    <thead>
      <tr>
        <th scope="col" class="align-middle text-center" style="width: 60px;"></th>
        <th scope="col" class="align-middle text-center" style="width: 60px;">ID</th>
        <th scope="col" class="align-middle" style="width: 60px;"></th>
        <th scope="col" class="align-middle">{$_('staff.name')}</th>
        <th scope="col" class="align-middle text-center">{$_('staff.priority')}</th>
      </tr>
    </thead>
    <tbody>
      {#if loading}
        <tr>
          <td colspan="5" class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </td>
        </tr>
      {:else}
        {#each staff as member (member.id)}
          <StaffRow 
            {member} 
            onEdit={handleEdit} 
            onDelete={handleDelete} 
          />
        {/each}
      {/if}
    </tbody>
  </table>
</div>
