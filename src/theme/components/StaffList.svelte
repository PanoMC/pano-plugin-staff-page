<style>
  .staff-card {
    transition:
      transform 0.3s ease,
      box-shadow 0.3s ease;
  }
  .staff-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1) !important;
  }
  .social-link {
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: all 0.2s;
    text-decoration: none;
  }
  .social-link:hover {
    transform: scale(1.1);
  }
  .staff-overlay {
    background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  .staff-grid-item:hover .staff-overlay {
    opacity: 1;
  }
  .hover-opacity-100:hover {
    opacity: 1 !important;
  }
</style>

{#if staff.length === 0}
  <NoContent text={$_('staff.no-staff')} />
  
{:else if viewMode === 'LIST'}
  <div class="staff-list">
    {#each staff as member}
      <div class="staff-item-list d-flex align-items-center p-3 mb-3 border rounded">
        <img
          src={member.avatarUrl || 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + member.name}
          alt={member.name}
          class="rounded-circle me-4"
          style="width: 80px; height: 80px; object-fit: cover;" />
        <div class="flex-grow-1">
          <h4 class="mb-1">{member.name}</h4>
          <span class="badge bg-primary-subtle text-primary mb-2">{member.role}</span>
          <p class="mb-0 small">{member.description || ''}</p>
        </div>
        <div class="socials d-flex gap-2">
          {#each parseSocial(member.socialLinks) as [platform, url]}
            <a
              href={url.startsWith('http') ? url : '#'}
              target="_blank"
              class="btn btn-sm btn-outline-secondary"
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
  <div class="row g-4">
    {#each staff as member}
      <div class="col-md-6 col-lg-4">
        <div class="staff-card text-center h-100 p-4 border rounded shadow-sm">
          <div class="staff-avatar-wrapper mb-3 position-relative d-inline-block">
            <img
              src={member.avatarUrl ||
                'https://api.dicebear.com/7.x/avataaars/svg?seed=' + member.name}
              alt={member.name}
              class="rounded-circle"
              style="width: 120px; height: 120px; object-fit: cover;" />
          </div>
          <h4 class="mb-1">{member.name}</h4>
          <div class="text-primary fw-bold mb-3">{member.role}</div>
          <p class="small mb-4">{member.description || ''}</p>
          <div class="socials d-flex justify-content-center gap-2 mt-auto">
            {#each parseSocial(member.socialLinks) as [platform, url]}
              <a
                href={url.startsWith('http') ? url : '#'}
                target="_blank"
                class="social-link"
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
  <div class="row g-3">
    {#each staff as member}
      <div class="col-6 col-md-4 col-lg-3">
        <div
          class="staff-grid-item position-relative overflow-hidden rounded ratio ratio-1x1 border">
          <img
            src={member.avatarUrl ||
              'https://api.dicebear.com/7.x/avataaars/svg?seed=' + member.name}
            alt={member.name}
            class="w-100 h-100 object-fit-cover" />
          <div class="staff-overlay p-3 d-flex flex-column justify-content-end text-white">
            <div class="fw-bold">{member.name}</div>
            <div class="small opacity-75">{member.role}</div>
            <div class="socials d-flex gap-2 mt-2">
              {#each parseSocial(member.socialLinks) as [platform, url]}
                <a
                  href={url.startsWith('http') ? url : '#'}
                  target="_blank"
                  class="text-white opacity-75 hover-opacity-100"
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
  import { _ } from '../../main';
  import { NoContent } from '@panomc/sdk/components/theme';

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
