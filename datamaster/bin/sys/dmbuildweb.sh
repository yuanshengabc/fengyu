#!/bin/bash
cd ${DMWEBPATH}
git pull
cnpm install
npm run build
