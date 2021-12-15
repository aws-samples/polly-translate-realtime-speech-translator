############################################################################
# Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this
# software and associated documentation files (the "Software"), to deal in the Software
# without restriction, including without limitation the rights to use, copy, modify,
# merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
# INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
# PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
############################################################################

#!/bin/bash

#Function to get the full path of scrip regardless of where the script is run from
realpath() {
    [[ $1 = /* ]] && echo "$1" || echo "$PWD/${1#./}"
}

SCRIPT=$(realpath "$0")
SCRIPTPATH=$(dirname "$SCRIPT")
CFN_TEMPLATE="$SCRIPTPATH/cftemplate-final.yml"
echo "Script folder : $SCRIPTPATH"

CODEBUCKET=$(aws cloudformation describe-stacks --stack-name vt-code --query "Stacks[0].Outputs[?OutputKey == 'VoiceTranslatorCodeBucket'].OutputValue" --output text)

aws s3 rm s3://$CODEBUCKET --recursive

aws cloudformation delete-stack --stack-name vt-code
aws cloudformation delete-stack --stack-name s2s-translator
