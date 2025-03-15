# Start the python django server
python manage.py runserver 0.0.0.0:8000

# After the local server is setup, use ngrok to route online traffic to the local django server
ngrok http --url=sharing-cool-sole.ngrok-free.app 8000

source ~/Documents/TFG/FakeNewsDetectorBackend/pythonVirtualEnv/bin/activate
