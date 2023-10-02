#!/bin/bash

SCRIPT_DIR=`dirname $0`/.. && SCRIPT_DIR=`(cd "$BASEDIR"; pwd)`
#echo "[DEBUG] $(date +'%F %T') SCRIPT_DIR=$SCRIPT_DIR"

if [ "$1" == "" ]; then echo " Usage:   $0 <RepoDir> [<OutputBase>]" && echo " Example: $0 /Users/dcqc/W/BL/monorepo-v1-bazel_latest_version" && exit 1; fi;

if [ "$1" != "" ]; then FIRST_ARG="$1"; fi;
#if [ "$FIRST_ARG" == "" ]; then FIRST_ARG="/Users/dcqc/W/BL/monorepo-v1-bazel_latest_version"; fi;
#echo "[DEBUG] $(date +'%Y-%m-%d %H:%M:%S') FIRST_ARG=$FIRST_ARG"

OSTYPE_UNAME=`uname`
if [[ "$OSTYPE_UNAME" == "Darwin"* ]]; then MD5SUM_TOOL="md5"; else MD5SUM_TOOL="md5sum"; fi;
if [[ "$OSTYPE_UNAME" == "Darwin"* ]]; then BAZEL_OR_DIR="/private/var/tmp"; else BAZEL_OR_DIR="~/.cache/bazel"; fi;

if [[ "$FIRST_ARG" =~ ^"$BAZEL_OR_DIR/_bazel_" ]]; then BAZEL_OB_BASENAME="$(basename $FIRST_ARG)";
else BAZEL_OB_BASENAME="`$MD5SUM_TOOL -q -s $FIRST_ARG`"; fi;
#echo "[DEBUG] $(date +%T) BAZEL_OB_BASENAME=$BAZEL_OB_BASENAME"

#SECOND_ARG="/private/var/tmp/_bazel_dcqc/035ffa8c8aec1e6f3e99e97581651e52";
if [ "$2" != "" ]; then SECOND_ARG="$2"; fi;
if [ "$SECOND_ARG" == "" ]; then SECOND_ARG="$BAZEL_OR_DIR/_bazel_$USER/$BAZEL_OB_BASENAME"; fi;
#echo "[DEBUG] $(date +%T) SECOND_ARG=$SECOND_ARG"

REPO_DIR="$FIRST_ARG"
OB_DIR="$SECOND_ARG"
echo "[DEBUG] $(date +'%F %T') Cleaning $REPO_DIR outputBase: $OB_DIR"

#[ -d $REPO_DIR ] && du -sh $REPO_DIR
[ -d $OB_DIR ] && du -sh $OB_DIR && echo "[DEBUG] $(date +%T) Will remove $OB_DIR - even when existing $REPO_DIR"
du -sh $REPO_DIR && cd $REPO_DIR && bazel clean --expunge && cd -
[ -d $REPO_DIR ] && du -sh $OB_DIR || rm -rf $OB_DIR && [ ! -d $OB_DIR ] && echo "[INFO] $(date +%T) $OB_DIR already removed!"

SUFFIX_CICD="BuildPath-BF"
if [[ "$REPO_DIR" == *"$SUFFIX_CICD" ]]; then (
  [ -d $REPO_DIR ] && echo "[WARN] Removing directory: $REPO_DIR" && rm -rf $REPO_DIR && \
  [ ! -d $REPO_DIR ] && echo "[WARN] $REPO_DIR has been removed!"
  [ -d ${REPO_DIR/$SUFFIX_CICD/} ] && rm -rf "${REPO_DIR/$SUFFIX_CICD/}" && echo "[WARN] ${REPO_DIR/$SUFFIX_CICD/} removed!"
  )
fi;
