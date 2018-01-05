const DashboardPlugin = require('webpack-dashboard/plugin');
config.plugins.push(new DashboardPlugin({port: 8080}));
