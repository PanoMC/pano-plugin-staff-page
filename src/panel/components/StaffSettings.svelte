{#if addon?.id === 'pano-plugin-staff-page'}
  <div class="card">
    <div class="card-header">
      {$_('settings.title') || 'Plugin Settings'}
    </div>
    <div class="card-body">
      <!-- Display Location -->
      <div class="row mb-4">
        <label class="col-md-4 col-form-label" for="displayLocation1">
          <span class="d-block">
            {$_('settings.display-location')}
          </span>
          <small class="text-muted">
            Where should the staff list be displayed?
          </small>
        </label>
        <div class="col-md-8">
          <div class="btn-group w-100" role="group">
            <input type="radio" class="btn-check" name="displayLocation" id="displayLocation1" value="THEME_PAGE" bind:group={config.displayLocation} />
            <label class="btn btn-outline-primary" for="displayLocation1">{$_('settings.display-location-page')}</label>

            <input type="radio" class="btn-check" name="displayLocation" id="displayLocation2" value="SUPPORT_PAGE" bind:group={config.displayLocation} />
            <label class="btn btn-outline-primary" for="displayLocation2">{$_('settings.display-location-support')}</label>
          </div>
        </div>
      </div>

      <!-- Page URL -->
      <div class="row mb-4" class:opacity-50={config.displayLocation !== 'THEME_PAGE'}>
        <label class="col-md-4 col-form-label" for="pageUrl">
          <span class="d-block">
            {$_('settings.page-url')}
          </span>
          <small class="text-muted">
            Public staff page will be accessible at this URL.
          </small>
        </label>
        <div class="col-md-8">
          <div class="input-group">
            <span class="input-group-text">/</span>
            <input 
              type="text" 
              id="pageUrl" 
              class="form-control" 
              bind:value={config.pageUrl} 
              disabled={config.displayLocation !== 'THEME_PAGE'} 
            />
          </div>
        </div>
      </div>

      <!-- View Mode -->
      <div class="row mb-4">
        <label class="col-md-4 col-form-label" for="viewMode1">
          <span class="d-block">
            {$_('settings.view-mode')}
          </span>
          <small class="text-muted">
            Choose how staff members are displayed.
          </small>
        </label>
        <div class="col-md-8">
          <div class="btn-group w-100" role="group">
            <input type="radio" class="btn-check" name="viewMode" id="viewMode1" value="LIST" bind:group={config.viewMode} />
            <label class="btn btn-outline-primary" for="viewMode1">{$_('view-modes.list')}</label>

            <input type="radio" class="btn-check" name="viewMode" id="viewMode2" value="CARD" bind:group={config.viewMode} />
            <label class="btn btn-outline-primary" for="viewMode2">{$_('view-modes.card')}</label>

            <input type="radio" class="btn-check" name="viewMode" id="viewMode3" value="GRID" bind:group={config.viewMode} />
            <label class="btn btn-outline-primary" for="viewMode3">{$_('view-modes.grid')}</label>
          </div>
        </div>
      </div>

      <div class="mt-4">
        <button class="btn btn-secondary" on:click={saveConfig} disabled={saving || !hasChanges}>
          {#if saving}
            <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
          {/if}
          {$_('common.save') || 'Save Changes'}
        </button>
      </div>
    </div>
  </div>
{/if}

<script>
  import { _ } from '../../main';
  import ApiUtil from '@panomc/sdk/utils/api';
  import { showToast } from '@panomc/sdk/toasts';

  export let addon;

  let config = addon?.config || {
    pageUrl: 'staff',
    viewMode: 'CARD',
    displayLocation: 'THEME_PAGE'
  };
  let saving = false;

  let initialConfig = JSON.parse(JSON.stringify(config));

  $: hasChanges = JSON.stringify(config) !== JSON.stringify(initialConfig);

  async function saveConfig() {
    if (saving) return;
    saving = true;
    try {
      await ApiUtil.put({ 
        path: '/api/panel/staff/config',
        body: config
      });
      addon.config = config; // Update local state for consistency
      initialConfig = JSON.parse(JSON.stringify(config)); // Update initial config to new saved state
      showToast($_('toasts.save-success'));
    } catch (e) {
      console.error('Failed to save config', e);
      showToast('Failed to save settings');
    } finally {
      saving = false;
    }
  }
</script>
