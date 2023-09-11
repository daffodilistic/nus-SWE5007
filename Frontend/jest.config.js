module.exports = {
  preset: '@vue/cli-plugin-unit-jest/presets/no-babel', // This is your Vue preset
  transform: {
    '^.+\\.js$': 'babel-jest', // Use Babel to transform .js files
  },
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
  },
  transformIgnorePatterns: ["/node_modules/(?!axios)"],
};
