require 'buildr/git_auto_version'
require 'buildr/gpg'

desc 'Filter for accessing keycloak secured services'
define 'keycloak-jaxrs-client-authfilter' do
  project.group = 'org.realityforge.keycloak.client.authfilter'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/keycloak-jaxrs-client-authfilter')
  pom.add_developer('realityforge', 'Peter Donald', 'peter@realityforge.org', ['Developer'])
  pom.provided_dependencies.concat [:javaee_api, :jsr305_annotations, :glassfish_embedded]

  compile.with :javaee_api, :jsr305_annotations, :keycloak_core, :keycloak_common, :keycloak_adapter_core, :keycloak_adapter_spi, :glassfish_embedded

  package(:jar)
  package(:sources)
  package(:javadoc)

  ipr.add_component_from_artifact(:idea_codestyle)
end
