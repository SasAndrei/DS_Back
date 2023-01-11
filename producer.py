import pika, os, signal, sys
import time
import calendar
import pandas
import json

def signal_handler(signal, frame):
    sys.exit(0)

def current_milli_time():
    return round(time.time() * 1000)

def main():
    signal.signal(signal.SIGINT, signal_handler)

    # Access the CLODUAMQP_URL environment variable and parse it (fallback to localhost)
    url = os.environ.get('CLOUDAMQP_URL', 'amqps://qkkgdxwt:ZfjzuqFpqq56-JCcMiPS9m7IqlBUr3xV@goose.rmq2.cloudamqp.com/qkkgdxwt')
    params = pika.URLParameters(url)
    connection = pika.BlockingConnection(params)
    channel = connection.channel() # start a channel

    df = pandas.read_csv('sensor.csv')

    js = {"timestamp":current_milli_time(),
        "device_id":"1",
        "measurement_value":df['0'][0]}

    y = json.dumps(js)

    print(y)

    k = 0

    while True:
        js = {"id": k % 3 + 1, "timestamp":current_milli_time(), "consumption":df['0'][k]}
        y = json.dumps(js)
        k = k + 1
        channel.basic_publish(exchange='',
                        routing_key='devices',
                        body=y)
        if k == 500:
            break
        time.sleep(100)

    connection.close()

if __name__ == "__main__":
    main()