<tr class:table-active={member.selected}>
  <!-- Actions Column -->
  <th scope="row" class="align-middle text-center" style="width: 60px;">
    <div class="dropdown position-static">
      <button
        type="button"
        class="btn btn-link px-2"
        data-bs-toggle="dropdown"
        use:tooltip={[$_('common.actions') || 'Actions']}
        aria-label={$_('common.actions') || 'Actions'}>
        <span class="fas fa-ellipsis-v"></span>
      </button>
      <div class="dropdown-menu animate__animated animate__fadeIn">
        <button type="button" class="dropdown-item" on:click={() => onEdit(member)}>
          <i class="fas fa-pen me-2"></i>
          {$_('common.edit')}
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

  <!-- Name (Combined with Avatar) -->
  <td class="align-middle">
    <button
      type="button"
      class="btn btn-link p-1 focus-ring rounded-pill d-inline-flex align-items-center gap-2 text-decoration-none"
      on:click={() => onEdit(member)}
      use:tooltip={[$_('common.edit')]}>
      {#if member.avatarUrl}
        <img
          src={member.avatarUrl}
          alt={member.name}
          class="rounded-circle shadow-sm"
          style="width: 32px; height: 32px; object-fit: cover;" />
      {:else}
        <div
          class="text-bg-light rounded-circle text-center"
          style="width: 32px; height: 32px; line-height: 32px;">
          <i class="fas fa-user"></i>
        </div>
      {/if}
      <div class="text-start pe-2">
        <span class="fw-bold">{member.name}</span>
      </div>
    </button>
  </td>

  <!-- Role -->
  <td class="align-middle text-center">
    <span class="badge text-bg-light">{member.role}</span>
  </td>

  <!-- Priority -->
  <td class="align-middle text-center">
    {member.priority}
  </td>
</tr>

<script>
  import { _ } from '../../main';
  import tooltip from '@panomc/sdk/utils/tooltip';

  export let member;
  export let onEdit;
  export let onDelete;
</script>
