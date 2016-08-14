
echo "ini stop all containers..."

sudo docker stop  $(sudo docker ps -q)

echo "end stop all containers..."