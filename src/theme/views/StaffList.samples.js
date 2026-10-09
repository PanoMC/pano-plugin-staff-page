// Sample data for the view catalogue (doc 02 section 7). Pure data.
export const notApplicable = ['loading', 'error'];

const staff = [
  { name: 'Alex', role: 'Owner', description: 'Runs the server.', avatarUrl: '', socialLinks: JSON.stringify({ discord: 'https://discord.gg/example', github: 'https://github.com/example' }) },
  { name: 'Steve', role: 'Moderator', description: 'Keeps chat friendly.', avatarUrl: '', socialLinks: JSON.stringify({ youtube: 'https://youtube.com/@example' }) },
  { name: 'Notch', role: 'Builder', description: '', avatarUrl: '', socialLinks: '{}' },
];

/** @type {import('@panomc/plugin-kit').Samples} */
export default {
  filled: { props: { staff, viewMode: 'CARD' } },
  empty: { props: { staff: [], viewMode: 'CARD' } },
  list: { label: 'List mode', props: { staff, viewMode: 'LIST' } },
  grid: { label: 'Grid mode', props: { staff, viewMode: 'GRID' } },
};
