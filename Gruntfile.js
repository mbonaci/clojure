module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    assemble: {
      options: {
        plugin: ['assemble-contrib-markdown', 'other/plugins/*']
      }
    }
  });

  grunt.loadNpmTasks('assemble' );
  grunt.loadNpmTasks('grunt-newer' );
  grunt.registerTask('default', ['newer:assemble']);

};