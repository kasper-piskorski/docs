# Files to copy from _jekyll to top level
$jekyll_files = [
    '_data',
    '_includes',
    '_layouts',
    'feed.xml',
    'search.json',
    'sitemap.xml',
    'tooltips.json',
    '_config.yml',
    'Gemfile'
]

# Files needed in _site only; to view the generated content.
$site_files = [
    'css',
    'fonts',
    'js',
    'img',
    'licenses'
]

# ---- Rake tasks

desc 'Install dependencies to build project'
task :dependencies do
    # Install build dependencies
    sh 'bundle install'
end

task :copy_template do
    # Copy template files
    $jekyll_files.each { |file|
        FileUtils.cp_r('_jekyll/'+file, file, :verbose => true)
    }
end

task :copy_assets do
    # Create dir for generated HTML
    #FileUtils.mkdir('_site')

    # Copy site assets
    $site_files.each { |asset|
        FileUtils.cp_r('_jekyll/'+asset, asset, :verbose => true)
    }
end

desc 'Clean up generated files'
task :clean do
    rm_rf '_site'
    $jekyll_files.each{ |file| rm_rf file }
    $site_files.each{ |file| rm_rf file }
end

desc 'Generate HTML and build site'
task :build => ['clean', 'copy_template', 'copy_assets'] do
    sh 'bundle exec jekyll build'
end

