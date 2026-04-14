#!/bin/bash
set -e
BASE="http://localhost:8080"
DELAY=2

step() {
  sleep $DELAY
}

echo "=== Login Player 1 ==="
P1=$(curl -s -X POST $BASE/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testUser1"}')
echo $P1 | python3 -m json.tool
SID1=$(echo $P1 | python3 -c "import sys,json; print(json.load(sys.stdin)['sessionId'])")

step
echo "=== Login Player 2 ==="
P2=$(curl -s -X POST $BASE/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testUser2"}')
echo $P2 | python3 -m json.tool
SID2=$(echo $P2 | python3 -c "import sys,json; print(json.load(sys.stdin)['sessionId'])")

step
echo "=== Create Round (Player 1) ==="
ROUND=$(curl -s -X POST $BASE/rounds \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\"}")
echo $ROUND | python3 -m json.tool
RID=$(echo $ROUND | python3 -c "import sys,json; print(json.load(sys.stdin)['round']['roundId'])")

step
echo "=== Join Round (Player 2) ==="
curl -s -X POST $BASE/rounds/join \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID2\",\"roundId\":$RID}" | python3 -m json.tool

step
echo "=== Start Round (Player 1) ==="
curl -s -X POST $BASE/rounds/start \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\",\"roundId\":$RID}" | python3 -m json.tool

step
echo "=== Move X: (0,0) ==="
curl -s -X POST $BASE/rounds/move \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\",\"roundId\":$RID,\"row\":0,\"col\":0}" | python3 -m json.tool

step
echo "=== Move O: (1,0) ==="
curl -s -X POST $BASE/rounds/move \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID2\",\"roundId\":$RID,\"row\":1,\"col\":0}" | python3 -m json.tool

step
echo "=== Move X: (0,1) ==="
curl -s -X POST $BASE/rounds/move \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\",\"roundId\":$RID,\"row\":0,\"col\":1}" | python3 -m json.tool

step
echo "=== Move O: (1,1) ==="
curl -s -X POST $BASE/rounds/move \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID2\",\"roundId\":$RID,\"row\":1,\"col\":1}" | python3 -m json.tool

step
echo "=== Move X: (0,2) — X wins top row ==="
curl -s -X POST $BASE/rounds/move \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\",\"roundId\":$RID,\"row\":0,\"col\":2}" | python3 -m json.tool

step
echo "=== Poll State ==="
curl -s $BASE/rounds/$RID | python3 -m json.tool

step
echo "=== Finish Round (Player 1) ==="
curl -s -X POST $BASE/rounds/finish \
  -H "Content-Type: application/json" \
  -d "{\"sessionId\":\"$SID1\",\"roundId\":$RID}" | python3 -m json.tool