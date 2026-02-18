<div class="modal fade" bind:this={$modalElement} tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">
          {$mode === 'edit' ? $_('staff.edit') : $_('staff.add-new')}
        </h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          on:click={hide}
          aria-label={$_('buttons.close') || 'Close'}></button>
      </div>
      <div class="modal-body">
        <div class="row g-3">
          <!-- Name (Full width, LG) -->
          <div class="col-12">
            <div class="form-floating">
              <input
                type="text"
                id="staffName"
                class="form-control form-control-lg"
                bind:value={$staff.name}
                placeholder={$_('staff.name')}
                required />
              <label for="staffName">{$_('staff.name')}</label>
            </div>
          </div>

          <!-- Role & Priority (Side by side) -->
          <div class="col-md-6">
            <div class="form-floating">
              <input
                type="text"
                id="staffRole"
                class="form-control"
                bind:value={$staff.role}
                placeholder={$_('staff.role')}
                required />
              <label for="staffRole">{$_('staff.role')}</label>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-floating">
              <input
                type="number"
                id="staffPriority"
                class="form-control"
                bind:value={$staff.priority}
                placeholder={$_('staff.priority')} />
              <label for="staffPriority">
                {$_('staff.priority')}
                {$_('common.optional')}
              </label>
            </div>
          </div>

          <!-- Avatar (Full width) -->
          <div class="col-12">
            <div class="form-floating mb-1">
              <input
                type="text"
                id="staffAvatar"
                class="form-control"
                bind:value={$staff.avatarUrl}
                placeholder="https://..." />
              <label for="staffAvatar">
                {$_('staff.avatar')}
                {$_('common.optional')}
              </label>
            </div>
            <button
              type="button"
              class="btn btn-link btn-sm text-decoration-none"
              on:click={useMinotar}>
              <i class="fas fa-magic me-1"></i>
              {$_('staff.use-minotar')}
            </button>
          </div>

          <!-- Description (Full width) -->
          <div class="col-12">
            <div class="form-floating">
              <textarea
                id="staffDescription"
                class="form-control"
                style="height: 100px"
                bind:value={$staff.description}
                placeholder={$_('staff.description')}></textarea>
              <label for="staffDescription">
                {$_('staff.description')}
                {$_('common.optional')}
              </label>
            </div>
          </div>

          <div class="col-12">
            <div class="d-flex align-items-center mb-2">
              <label class="form-label mb-0" for="socialInput0">
                {$_('staff.social-links')}
                {$_('common.optional')}
              </label>
              <button
                class="btn btn-sm btn-primary ms-auto"
                on:click={addSocial}
                aria-label="Add social link">
                {$_('common.add') || 'Add Link'}
              </button>
            </div>
            {#each $staff.socialLinks as link, i}
              <div class="hstack gap-2">
                <div class="input-group mb-2">
                  <input
                    type="text"
                    id="socialInput{i}"
                    class="form-control"
                    style="max-width: 150px;"
                    placeholder={$_('common.platform')}
                    bind:value={link.platform} />
                  <input
                    type="text"
                    class="form-control"
                    placeholder={$_('staff.url-placeholder')}
                    bind:value={link.url}
                    aria-label="Social link URL" />
                </div>

                <button
                  class="btn-close"
                  on:click={() => removeSocial(i)}
                  aria-label="Remove social link">
                </button>
              </div>
            {/each}
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button
          class="btn w-100"
          class:btn-secondary={$mode === 'create'}
          class:btn-primary={$mode === 'edit'}
          on:click={save}
          disabled={saving || !isFormValid || ($mode === 'edit' && !hasChanges)}>
          {#if saving}
            <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"
            ></span>
          {/if}
          {$mode === 'edit' ? $_('common.save') : $_('common.add')}
        </button>
      </div>
    </div>
  </div>
</div>

<script context="module">
  import { writable, get } from 'svelte/store';

  const modalElement = writable();
  const mode = writable('create');
  const staff = writable({ socialLinks: [] });
  const initialState = writable('');

  let callback = () => {};
  let modal;

  export function show(newMode, member = null) {
    mode.set(newMode);

    let socialLinks = [];
    if (member && member.socialLinks) {
      try {
        const parsed = JSON.parse(member.socialLinks);
        socialLinks = Object.entries(parsed).map(([platform, url]) => ({ platform, url }));
      } catch (e) {
        socialLinks = [];
      }
    }

    const initialData = {
      id: member?.id || null,
      name: member?.name || '',
      role: member?.role || '',
      avatarUrl: member?.avatarUrl || '',
      priority: member?.priority || 0,
      description: member?.description ?? '',
      socialLinks: socialLinks,
    };

    staff.set(initialData);
    initialState.set(JSON.stringify(initialData));

    modal = new window.bootstrap.Modal(get(modalElement), {
      backdrop: 'static',
      keyboard: false,
    });
    modal.show();
  }

  export function hide() {
    modal.hide();
  }

  export function setCallback(newCallback) {
    callback = newCallback;
  }
</script>

<script>
  import { _ } from '../../main';
  import ApiUtil from '@panomc/sdk/utils/api';
  import { showToast } from '@panomc/sdk/toasts';

  let saving = false;

  $: currentState = JSON.stringify($staff);
  $: hasChanges = currentState !== $initialState;

  $: isFormValid = ($staff.name || '').trim().length > 0 && ($staff.role || '').trim().length > 0;

  function addSocial() {
    staff.update((s) => {
      s.socialLinks = [...(s.socialLinks || []), { platform: '', url: '' }];
      return s;
    });
  }

  function removeSocial(index) {
    staff.update((s) => {
      s.socialLinks = (s.socialLinks || []).filter((_, i) => i !== index);
      return s;
    });
  }

  function useMinotar() {
    if ($staff.name) {
      $staff.avatarUrl = `https://minotar.net/avatar/${$staff.name.trim()}`;
    } else {
      showToast($_('staff.enter-name-first'));
    }
  }

  async function save() {
    if (saving || !isFormValid) return;

    saving = true;
    const currentStaff = get(staff);
    const currentMode = get(mode);

    const socialLinksObj = {};
    if (currentStaff.socialLinks) {
      currentStaff.socialLinks.forEach((s) => {
        if (s.platform && s.url) socialLinksObj[s.platform] = s.url;
      });
    }

    const body = {
      name: currentStaff.name.trim(),
      role: currentStaff.role.trim(),
      avatarUrl: currentStaff.avatarUrl?.trim() ?? '',
      priority: parseInt(currentStaff.priority) || 0,
      description: currentStaff.description?.trim() ?? '',
      socialLinks: JSON.stringify(socialLinksObj),
    };

    try {
      if (currentMode === 'edit') {
        await ApiUtil.put({ path: `/api/panel/staffs/${currentStaff.id}`, body });
      } else {
        await ApiUtil.post({ path: '/api/panel/staffs', body });
      }

      showToast(currentMode === 'edit' ? $_('toasts.staff-updated') : $_('toasts.staff-created'));

      if (callback) {
        callback();
      }

      hide();
    } catch (e) {
      console.error('Failed to save staff member', e);
      showToast($_('toasts.staff-error'));
    } finally {
      saving = false;
    }
  }
</script>
