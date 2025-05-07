docker build -t 12factor_todo .

docker tag 12factor_todo 194722421717.dkr.ecr.eu-north-1.amazonaws.com/12factor_todo

aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin 194722421717.dkr.ecr.eu-north-1.amazonaws.com

docker push 194722421717.dkr.ecr.eu-north-1.amazonaws.com/12factor_todo