#!/bin/bash
GH_URL="https://raw.githubusercontent.com/ducquoc/jutil-dq/master/snippets-dq/ssocreds.py"
GL_URL="https://gitlab.com/ducquoc/jutil-dq/-/raw/master/snippets-dq/ssocreds.py?inline=false"
curl -s "$GL_URL" -C - -o ssocreds.py && chmod +x ssocreds.py
eval ./ssocreds.py "$@"
