require 'buildr/git_auto_version'
require 'buildr/gpg'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

desc 'Filter for accessing keycloak secured services'
define 'keycloak-jaxrs-client-authfilter' do
  project.group = 'org.realityforge.keycloak.client.authfilter'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'
  compile.options.warnings = true
  compile.options.other = %w(-Werror -Xmaxerrs 10000 -Xmaxwarns 10000)

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/keycloak-jaxrs-client-authfilter')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat [:javaee_api, :javax_annotations]

  compile.with :javaee_api, :javax_annotations

  package(:jar)
  package(:sources)
  package(:javadoc)

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all -Werror -Xmaxerrs 10000 -Xmaxwarns 10000')
end
