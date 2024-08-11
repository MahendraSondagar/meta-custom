# Python script to display the system info 
# !/usr/bin/python3.8

import platform 

data = platform.uname()

print(f"System :{data.system}")
print(f"Node: {data.node}")
print(f"Release : {data.release}")
print(f"version: {data.version}")
print(f"Machine: {data.machine}")
print(f"Processor: {data.processor}")


