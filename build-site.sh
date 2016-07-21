#!/bin/bash

create_link() {
    local filename=$(basename $1 .md)
    local output="- [$filename] ($1)"

    echo $output >> $2
}

cat navigation.header > navigation.md

for i in $(find documentation/ -name '*.md')
do
    create_link $i navigation.md
done

jekyll build
