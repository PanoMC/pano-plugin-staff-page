<tr class:table-active={member.selected}>
  <!-- Actions Column -->
  <th scope="row" class="align-middle text-center" style="width: 60px;">
    <div class="dropdown position-static">
      <button
        type="button"
        class="btn btn-link px-2"
        data-bs-toggle="dropdown"
        title={$_('common.actions') || 'Actions'}
        aria-label={$_('common.actions') || 'Actions'}>
        <span class="fas fa-ellipsis-v"></span>
      </button>
      <div class="dropdown-menu animate__animated animate__fadeIn">
        <button type="button" class="dropdown-item" on:click={() => onEdit(member)}>
          <i class="fas fa-pen me-2"></i>
          {$_('staff.edit')}
        </button>
        <button type="button" class="dropdown-item text-danger" on:click={() => onDelete(member)}>
          <i class="fas fa-trash me-2"></i>
          {$_('staff.delete')}
        </button>
      </div>
    </div>
  </th>

  <!-- ID Column -->
  <td class="align-middle text-center" style="width: 60px;">
    <code>{member.id}</code>
  </td>

  <!-- Avatar -->
  <td class="align-middle cursor-pointer" style="width: 60px;" on:click={() => onEdit(member)}>
    {#if member.avatarUrl}
      <img src={member.avatarUrl} alt={member.name} class="rounded-circle" style="width: 32px; height: 32px; object-fit: cover;" />
    {:else}
      <div class="bg-light rounded-circle text-center" style="width: 32px; height: 32px; line-height: 32px;">
        <i class="fas fa-user text-secondary"></i>
      </div>
    {/if}
  </td>

  <!-- Name & Role -->
  <td class="align-middle">
    <button
      type="button"
      class="btn btn-link p-0 focus-ring rounded text-decoration-none d-block text-truncate fw-bold text-start"
      on:click={() => onEdit(member)}
      title={member.name}>
      {member.name}
    </button>
    <small class="text-muted">{member.role}</small>
  </td>

  <!-- Priority -->
  <td class="align-middle text-center cursor-pointer" on:click={() => onEdit(member)}>
    <span class="badge bg-light text-dark border">
      {member.priority}
    </span>
  </td>
</tr>

<script>
  import { _ } from '../../main';

  export let member;
  export let onEdit;
  export let onDelete;
</script>

<style>
  .cursor-pointer {
    cursor: pointer;
  }
</style>
