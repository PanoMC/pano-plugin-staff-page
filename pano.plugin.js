// Plugin-level options of the Pano plugin kit (@panomc/plugin-kit). The namespace is `staff-page` (the plugin id minus
// `pano-plugin-`); the views are the files of src/theme/views.
export default {
  styles: {
    // StaffList keeps the avatar sizes (80, 120 px, object-fit) it always set inline.
    styleAttrAllow: ['StaffList'],
  },
};
