const CracoLessPlugin = require("craco-less");

module.exports = {
  plugins: [
    {
      plugin: CracoLessPlugin,
      options: {
        lessLoaderOptions: {
          lessOptions: {
            modifyVars: {
              // "@primary-color": "#9DCE74",
              "@primary-color": "#43AAE1",
              "@font-family": "regularFont",
            },
            javascriptEnabled: true,
          },
        },
      },
    },
  ],
};
