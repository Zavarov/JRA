#!/bin/bash
cd core/buildSrc
rm -r build
gradle

cd ../..

cd jra/buildSrc
rm -r build
gradle
