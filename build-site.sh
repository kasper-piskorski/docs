#!/bin/bash

create_link() {
    local filename=$(basename $1 .md)
    local directories=$(dirname $1)
    local output="- [$directories/$filename]($directories/$filename.html)"

    local category=$(basename $directories)
    category="$(echo $category | sed -e 's/documentation//')"

    if [ $category ]
    then
        echo "- $category" >> $2
        output='\t'$output
    fi

    echo -e $output >> $2
}

cat navigation.header > navigation.md

for i in $(find documentation/ -name '*.md' | sort -r)
do
    create_link $i navigation.md
done

jekyll build
