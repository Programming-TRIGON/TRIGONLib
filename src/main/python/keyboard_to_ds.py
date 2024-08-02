# Copyright (c) 2023 FRC 5990
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import math

import pyvjoy
import win32api
import win32con

device = pyvjoy.VJoyDevice(1)
device.reset()

keys = [0x1B, win32con.VK_F1, win32con.VK_F2, win32con.VK_F3, win32con.VK_F4, win32con.VK_F5, win32con.VK_F6,
        win32con.VK_F7, win32con.VK_F8, win32con.VK_F9, win32con.VK_F10, win32con.VK_F11, win32con.VK_F12,
        win32con.VK_DELETE, 0xC0, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 0xBD, 0xBB, win32con.VK_BACK,
        win32con.VK_TAB, 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
        0xBA, 0xDE, win32con.VK_LSHIFT, 'z', 'x', 'c', 'v', 'b', 'n', 'm', 0xBC, 0xBE, 0xBF, win32con.VK_RSHIFT,
        win32con.VK_LCONTROL, win32con.VK_LMENU, win32con.VK_RMENU, win32con.VK_RCONTROL, win32con.VK_LEFT,
        win32con.VK_RIGHT, win32con.VK_UP, win32con.VK_DOWN, win32con.VK_NUMPAD0, win32con.VK_NUMPAD1,
        win32con.VK_NUMPAD2, win32con.VK_NUMPAD3, win32con.VK_NUMPAD4, win32con.VK_NUMPAD5, win32con.VK_NUMPAD6,
        win32con.VK_NUMPAD7, win32con.VK_NUMPAD8, win32con.VK_NUMPAD9]
axis = [
    ['r', 't', 'y', 'u', 'i', 'o', 'p', 'a'],
    ['s', 'd', 'f', 'g', 'h', 'j', 'k', 'l'],
    [0xBA, 0xDE, win32con.VK_LSHIFT, 'z', 'x', 'c', 'v', 'b'],
    ['n', 'm', 0xBC, 0xBE, 0xBF, win32con.VK_RSHIFT, win32con.VK_LCONTROL, win32con.VK_LMENU],
    [win32con.VK_RMENU, win32con.VK_RCONTROL, win32con.VK_LEFT, win32con.VK_RIGHT,
     win32con.VK_UP, win32con.VK_DOWN, win32con.VK_NUMPAD0, win32con.VK_NUMPAD1],
    [win32con.VK_NUMPAD2, win32con.VK_NUMPAD3, win32con.VK_NUMPAD4, win32con.VK_NUMPAD5,
     win32con.VK_NUMPAD6, win32con.VK_NUMPAD7, win32con.VK_NUMPAD8, win32con.VK_NUMPAD9]
]

# Create dictionary of keys with name, button number, state, and key code
keys = [
    {
        'name': keys[i],
        'button': i + 1,
        'state': False,
        'key_code': ord(keys[i].upper()) if isinstance(keys[i], str) else keys[i]
    } for i in range(len(keys))
]

# Convert axis elements to their corresponding ASCII codes if they are strings
for i in range(len(axis)):
    for j in range(len(axis[i])):
        axis[i][j] = ord(axis[i][j].upper()) if isinstance(axis[i][j], str) else axis[i][j]


# Convert boolean array to float value
def bool_arr_to_float(bool_arr):
    value = 1 / 256
    result = 0
    for i in range(len(bool_arr)):
        if bool_arr[i]:
            result += value
        value *= 2
    return result


# Convert value to vJoy axis value
def convert_to_vjoy_axis(value):
    print(round(value * 32767, 0))
    return math.floor(round(value * 32767, 0))


# Get vJoy HID usage code of an axis ID
def get_axle(id):
    if id == 0:
        return pyvjoy.HID_USAGE_X
    elif id == 1:
        return pyvjoy.HID_USAGE_Y
    elif id == 2:
        return pyvjoy.HID_USAGE_Z
    elif id == 3:
        return pyvjoy.HID_USAGE_RX
    elif id == 4:
        return pyvjoy.HID_USAGE_RY
    elif id == 5:
        return pyvjoy.HID_USAGE_RZ


# Update vJoy axis with new value
def update_axle(axle):
    keys = axis[axle]
    device.set_axis(
        get_axle(axle),
        convert_to_vjoy_axis(bool_arr_to_float([win32api.GetAsyncKeyState(key) & 0x8000 for key in keys]))
    )


# Get axis ID of a key
def get_axle_of_key(key):
    for i in range(len(axis)):
        if key in axis[i]:
            return i
    return -1


# Check if a key is in an axis
def is_in_axis(key):
    return get_axle_of_key(key) != -1


# Reset vJoy axes to their neutral position
for i in range(6):
    device.set_axis(get_axle(i), -convert_to_vjoy_axis(bool_arr_to_float([False] * 8)))

# Monitor keys and update vJoy as needed
while True:
    for key in keys:
        if win32api.GetAsyncKeyState(key['key_code']) & 0x8000:
            if key['state']:
                continue
            device.set_button(key['button'], True)
            if is_in_axis(key['key_code']):
                update_axle(get_axle_of_key(key['key_code']))
            key['state'] = True
            print(key)
        else:
            if not key['state']:
                continue
            device.set_button(key['button'], False)
            if is_in_axis(key['key_code']):
                update_axle(get_axle_of_key(key['key_code']))
            key['state'] = False
            print(key)
