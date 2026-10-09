<style>
  .staff-page-staff-list__social-link {
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    text-decoration: none;
  }
  .staff-page-staff-list__overlay {
    background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  .staff-page-staff-list__grid-item:hover .staff-page-staff-list__overlay {
    opacity: 1;
  }
  .staff-page-staff-list__social-hover:hover {
    opacity: 1 !important;
  }
</style>

{#if staff.length === 0}
  <NoContent text={$_('staff.no-staff')} />
{:else if viewMode === 'LIST'}
  <div class="staff-page-staff-list">
    {#each staff as member}
      <div class="staff-page-staff-list__item d-flex align-items-center p-3 mb-3 border rounded">
        <img
          src={member.avatarUrl || 'https://minotar.net/helm/' + member.name + '/80'}
          alt={member.name}
          class="staff-page-staff-list__image rounded-circle me-4"
          style="width: 80px; height: 80px; object-fit: cover;" />
        <div class="flex-grow-1">
          <h4 class="staff-page-staff-list__title mb-1">{member.name}</h4>
          <span class="staff-page-staff-list__badge badge text-bg-primary mb-2">{member.role}</span>
          <p class="mb-0 small">{member.description || ''}</p>
        </div>
        <div class="staff-page-staff-list__socials d-flex gap-2">
          {#each parseSocial(member.socialLinks) as [platform, url]}
            <a
              href={url.startsWith('http') ? url : '#'}
              target="_blank"
              class="staff-page-staff-list__action btn btn-sm btn-link link-secondary"
              title={platform}
              aria-label={platform}>
              <i class={getSocialIcon(platform)}></i>
            </a>
          {/each}
        </div>
      </div>
    {/each}
  </div>
{:else if viewMode === 'CARD'}
  <div class="staff-page-staff-list row g-3">
    {#each staff as member}
      <div class="col-md-6 col-lg-4">
        <div class="staff-page-staff-list__card text-center h-100 p-3 border rounded">
          <div class="staff-page-staff-list__avatar mb-3 position-relative d-inline-block">
            <img
              src={member.avatarUrl || 'https://minotar.net/helm/' + member.name + '/120'}
              alt={member.name}
              class="staff-page-staff-list__image-2 rounded-circle"
              style="width: 120px; height: 120px; object-fit: cover;" />
          </div>
          <h4 class="staff-page-staff-list__title-2 mb-1">{member.name}</h4>
          <div class="staff-page-staff-list__badge-2 badge text-bg-primary mx-auto mb-3">{member.role}</div>
          <p class="mb-3">{member.description || ''}</p>
          <div class="staff-page-staff-list__socials d-flex justify-content-center gap-2 mt-auto">
            {#each parseSocial(member.socialLinks) as [platform, url]}
              <a
                href={url.startsWith('http') ? url : '#'}
                target="_blank"
                class="staff-page-staff-list__social-link"
                title={platform}
                aria-label={platform}>
                <i class={getSocialIcon(platform)}></i>
              </a>
            {/each}
          </div>
        </div>
      </div>
    {/each}
  </div>
{:else}
  <!-- GRID / SQUARE -->
  <div class="staff-page-staff-list row g-3">
    {#each staff as member}
      <div class="col-6 col-md-4 col-lg-3">
        <div
          class="staff-page-staff-list__grid-item position-relative overflow-hidden rounded ratio ratio-1x1 border">
          <img
            src={member.avatarUrl || 'https://minotar.net/helm/' + member.name + '/256'}
            alt={member.name}
            class="staff-page-staff-list__image-3 w-100 h-100 object-fit-cover" />
          <div class="staff-page-staff-list__overlay p-3 d-flex flex-column justify-content-end text-white">
            <div class="fw-bold">{member.name}</div>
            <div class="small opacity-75">{member.role}</div>
            <div class="staff-page-staff-list__socials d-flex gap-3 mt-2">
              {#each parseSocial(member.socialLinks) as [platform, url]}
                <a
                  href={url.startsWith('http') ? url : '#'}
                  target="_blank"
                  class="text-white opacity-75 staff-page-staff-list__social-hover"
                  title={platform}
                  aria-label={platform}>
                  <i class={getSocialIcon(platform)}></i>
                </a>
              {/each}
            </div>
          </div>
        </div>
      </div>
    {/each}
  </div>
{/if}

<script>
  import { derived } from 'svelte/store';
  import { _ as i18n } from '@panomc/sdk/utils/language';
  import { NoContent } from '@panomc/sdk/components/theme';

  // plugin translations: $_('key') reads plugins.pano-plugin-staff-page.key
  const _ = derived(i18n, ($_fn) => (key, options) => $_fn(`plugins.pano-plugin-staff-page.${key}`, options));

  export let staff = [];
  export let viewMode = 'CARD';

  function getSocialIcon(platform) {
    const p = platform.toLowerCase();
    if (p.includes('discord')) return 'fab fa-discord';
    if (p.includes('twitter') || p.includes('x')) return 'fab fa-x-twitter';
    if (p.includes('instagram')) return 'fab fa-instagram';
    if (p.includes('github')) return 'fab fa-github';
    if (p.includes('youtube')) return 'fab fa-youtube';
    if (p.includes('twitch')) return 'fab fa-twitch';
    return 'fas fa-link';
  }

  function parseSocial(json) {
    try {
      return Object.entries(JSON.parse(json));
    } catch (e) {
      return [];
    }
  }
</script>
