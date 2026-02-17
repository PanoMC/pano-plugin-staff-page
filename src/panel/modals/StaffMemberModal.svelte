<script>
  import { createEventDispatcher, onMount, onDestroy } from 'svelte';
  import { _ } from '../../main';
  import ApiUtil from '@panomc/sdk/utils/api';
  import { showToast } from '@panomc/sdk/toasts';

  export let member = null;
  const dispatch = createEventDispatcher();

  let modalElement;
  let modalInstance;

  let name = '';
  let role = '';
  let avatarUrl = '';
  let priority = 0;
  let description = '';
  let socialLinks = []; // [{platform: 'discord', url: '...'}]
  let saving = false;

  let initialState = '';

  $: currentState = JSON.stringify({ name, role, avatarUrl, priority, description, socialLinks });
  $: hasChanges = currentState !== initialState;

  onMount(() => {
    if (member) {
      name = member.name || '';
      role = member.role || '';
      avatarUrl = member.avatarUrl || '';
      priority = member.priority || 0;
      description = member.description || '';
      try {
        const parsed = JSON.parse(member.socialLinks);
        socialLinks = Object.entries(parsed).map(([platform, url]) => ({ platform, url }));
      } catch (e) {
        socialLinks = [];
      }
    }
    
    initialState = JSON.stringify({ name, role, avatarUrl, priority, description, socialLinks });

    if (typeof window !== 'undefined' && window.bootstrap) {
      modalInstance = new window.bootstrap.Modal(modalElement, {
        backdrop: true, // Allow closing by clicking backdrop
        keyboard: true
      });
      modalInstance.show();

      modalElement.addEventListener('hidden.bs.modal', () => {
        dispatch('close');
      });
    }
  });

  onDestroy(() => {
    if (modalInstance) {
      modalInstance.hide();
    }
  });

  function close() {
    if (modalInstance) {
      modalInstance.hide();
    } else {
      dispatch('close');
    }
  }

  function addSocial() {
    socialLinks = [...socialLinks, { platform: '', url: '' }];
  }

  function removeSocial(index) {
    socialLinks = socialLinks.filter((_, i) => i !== index);
  }

  async function save() {
    saving = true;
    const socialLinksObj = {};
    socialLinks.forEach(s => {
      if (s.platform && s.url) socialLinksObj[s.platform] = s.url;
    });

    const body = {
      name,
      role,
      avatarUrl: avatarUrl || null,
      priority,
      description: description || null,
      socialLinks: JSON.stringify(socialLinksObj)
    };

    try {
      if (member) {
        await ApiUtil.put({ path: `/api/panel/staff/${member.id}`, body });
      } else {
        await ApiUtil.post({ path: '/api/panel/staff', body });
      }
      showToast(member ? $_('toasts.staff-updated') : $_('toasts.staff-created'));
      dispatch('success');
      close();
    } catch (e) {
      console.error('Failed to save staff member', e);
      showToast('Failed to save staff member');
    } finally {
      saving = false;
    }
  }

  $: isFormValid = name.trim() && role.trim() && avatarUrl.trim() && description.trim();
</script>

<div class="modal fade" bind:this={modalElement} tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">{member ? $_('staff.edit') : $_('staff.add-new')}</h5>
        <button type="button" class="btn-close" on:click={close} aria-label={$_('common.cancel') || 'Close'}></button>
      </div>
      <div class="modal-body">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label" for="staffName">{$_('staff.name')}</label>
            <input type="text" id="staffName" class="form-control" bind:value={name} required />
          </div>
          <div class="col-md-6">
            <label class="form-label" for="staffRole">{$_('staff.role')}</label>
            <input type="text" id="staffRole" class="form-control" bind:value={role} required />
          </div>
          <div class="col-md-8">
            <label class="form-label" for="staffAvatar">{$_('staff.avatar')}</label>
            <input type="text" id="staffAvatar" class="form-control" bind:value={avatarUrl} placeholder="https://..." />
          </div>
          <div class="col-md-4">
            <label class="form-label" for="staffPriority">{$_('staff.priority')}</label>
            <input type="number" id="staffPriority" class="form-control" bind:value={priority} />
          </div>
          <div class="col-12">
            <label class="form-label" for="staffDescription">{$_('staff.description')}</label>
            <textarea id="staffDescription" class="form-control" bind:value={description} rows="2"></textarea>
          </div>
          
          <div class="col-12">
            <hr />
            <div class="d-flex align-items-center mb-2">
              <label class="form-label mb-0" for="socialInput0">{$_('staff.social-links')}</label>
              <button class="btn btn-sm btn-link ms-auto" on:click={addSocial} aria-label="Add social link">
                <i class="fas fa-plus"></i> {$_('common.add') || 'Add Link'}
              </button>
            </div>
            {#each socialLinks as link, i}
              <div class="input-group mb-2">
                <input type="text" id="socialInput{i}" class="form-control" style="max-width: 150px;" placeholder="Platform" bind:value={link.platform} />
                <input type="text" class="form-control" placeholder="URL or Username" bind:value={link.url} aria-label="Social link URL" />
                <button class="btn btn-outline-danger" on:click={() => removeSocial(i)} aria-label="Remove social link">
                  <i class="fas fa-times"></i>
                </button>
              </div>
            {/each}
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-light me-2" on:click={close}>{$_('common.cancel') || 'Cancel'}</button>
        <button class="btn btn-primary" on:click={save} disabled={saving || !isFormValid || (member && !hasChanges)}>
          {#if saving}
            <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
          {/if}
          {member ? $_('common.save') : $_('staff.add-new')}
        </button>
      </div>
    </div>
  </div>
</div>
