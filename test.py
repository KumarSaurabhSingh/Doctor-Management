import requests
import sys
import io
import json
import string, random
from datetime import datetime,timedelta



def get_request(url):
    status = False
    status_code = 500
    response = None
    try:
        res = requests.get(url)
        
        status_code = res.status_code
        if status_code == 200:
            status = True
        response = res.json()
    except Exception as e:
        try:
            status = False
            status_code = 400
            response = res.text
        except:
            pass
        status = False
        status_code = 500
        response = ""
    
    return status, response, status_code

def post_request(url, data):
    status = False
    status_code = 500
    response = None
    try:
        json_data = json.dumps(data)
        headers = {'Content-Type': 'application/json'}
        res = requests.post(url, data=json_data, headers=headers)
        status = True
        status_code = res.status_code
        response = res.json()
        
    except Exception as e:
        try:
            status = False
            status_code = res.status_code
            response = res.text
        except:
            pass
        status = False
        status_code = 500
        response = ""
    
    return status, response, status_code


#function to generate random names
def generate_random_string(length):
    letters = string.ascii_letters
    random_string = ''.join(random.choice(letters) for _ in range(length))
    return random_string

def generate_random_int_between_lengths(max_value):
    min_value = 0
    random_integer = random.randint(min_value, max_value)
    return random_integer

def generate_random_float_between_lengths(max_value):
    min_value = 1
    random_float = round(random.uniform(min_value, max_value), 2)
    return random_float

def check_type(value):
    if type(value) is float:
        return 'float'
    elif type(value) is int:
        return 'int'
    elif type(value) is str:
        return 'str'
    elif type(value) is bool:
        return 'bool'
    elif type(value) is dict:
        return 'dict'
    elif type(value) is list:
        return 'list'
    else:
        return 'None'

def generate_randomg_payload(payload, empkey=""):
    output = {}
    for obj in payload:
        key = obj['key']
        value = ""
        val_type = obj['type']
        if obj['is_fixed'] == True:
            value = obj['value']
        elif empkey != key:
            if val_type == 'str':
                value = generate_random_string(obj['len'])
            elif val_type == 'int':
                value = generate_random_int_between_lengths(obj['len'])
            elif val_type == 'float':
                value = generate_random_float_between_lengths(obj['len'])
            elif val_type == 'bool':
                value = True
        output[key] = value
    return output

def compare_objects_core(payload, response):
    ispassed = True
    msg = ""
    for key in payload:
        if key['key'] in response:
            if check_type(response[key['key']]) == key['type'] or response[key['key']] == None:
                if ispassed != False: ispassed = True
            else:
                ispassed = False
                msg += '<br> "' + str(key['key']) + '" should be "' + str(key['type']) + '"'
        else:
            ispassed = False
            msg += '<br> "' + str(key['key']) + '" is missing in your response'
    return ispassed, msg

def compare_objects(payload, response):
    ispassed = False
    if check_type(response) == 'list':
        if len(response) > 0:
            obj = response[0]
            ispassed, msg = compare_objects_core(payload, obj)
        else:
            msg = "<br> Your Response has 0 records"
    elif check_type(response) == 'dict':
        obj = response
        ispassed, msg = compare_objects_core(payload, obj)
    else:
        msg = "<br> Your Response is not in json format: <br>" + str(response)
    return ispassed, msg


def validate_post_request_custom(payload, url, success = False):
    finalpayloads = []
    msg = ""
    ispassed = False
    if success == False:
        for obj in payload:
            if obj['require'] == True and obj['is_fixed'] == False:
                key = obj['key']
                finalpayloads.append({'data': generate_randomg_payload(payload, key), '200': False})
    
    finalpayloads.append({'data': generate_randomg_payload(payload), '200': success})
    msg += "<br>Running Post request <br>endpoint: " + url
    res = None
    output = ""
    for data in finalpayloads:
        output = "<br> Sending Payload: " + str(data['data'])
        if data['200'] == True:
            output += "<br> Expected status code: 200"
        else:
            output += "<br> Expected status code: (403 or 400)"
        status, response, status_code = post_request(url, data['data'])
        res = response
        if status:
            if success == False and status_code != 200 and status_code != 500 and ispassed == False:
                output += '<br> API Status code: ' + str(status_code)
                ispassed = True
            elif status_code == 200 and ispassed == False and success == True:
                output += '<br> API Status code: ' + str(status_code)
                ispassed = True
            else:
                output += '<br> API Status code: ' + str(status_code)
        else:
            output += '<br> API Status code: ' + str(status_code)
        if ispassed == False:
            msg += output
            break
    if ispassed:
        msg += output
    return ispassed, msg, res

def site_check(baseurl):
    try:
        res = requests.get(baseurl)
        status_code = res.status_code
        if status_code == 500:
            return False
    except:
        pass
    return True

def test1(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors/create'
    name=generate_random_string(10)
    email=name+'@gmail.com'
    try:
        payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': ''},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
        ]
        ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, False)
        if ispassed:  message += "<br>Passed: Payload sent with blank name"
        
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':''},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, False)
            if ispassed: message += "<br>Passed: Payload sent with blank email"
            
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, False)
            if ispassed: message += "<br>Passed: Payload sent with blank specialization"
        
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, False)
            if ispassed: message += "<br>Passed: Payload sent with blank weeklySchedule"
            
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": ""},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, False)
            if ispassed: message += "<br>Passed: Payload sent with wrong weeklySchedule"
        
        
        if ispassed:
            
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "09:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]

            resobj = [
                {'key': 'id', 'type': 'int'},
                {'key': 'name', 'type': 'str'},
                {'key': 'email', 'type': 'str'},
                {'key': 'specialization', 'type': 'str'},
                {'key': 'weeklySchedule', 'type': 'list'},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl, True)
            if ispassed: 
                message += "<br>Passed: Payload sent with correct values"
                ispassed, msg = compare_objects(resobj, res)
            
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
            
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test2(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'},
            
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            ispassed, msg = compare_objects(resobj, res)
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test3(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                    specialization=obj['specialization']
                    break
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/search?specialization=abc')
            if ispassed == False:
                ispassed=True
                message += "<br> Passed: specialization sent wrong"
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/search?specialization=' + str(specialization))
            if ispassed: 
                ispassed, msg = compare_objects(resobj, res)
                message += "<br> Passed: Record fetched successfully"
                message += "<br> Response: <br>" + str(res)
            else:
                message += "<br> Endpoin failed: " + baseurl + suburl + '/search?specialization=' + str(specialization)
                message += "<br> Error in doctor by specialization"
        
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test4(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                    specialization=obj['specialization']
                    break
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/1000')
            if ispassed == False:
                ispassed=True
                message += "<br> Passed: getting doctor by wrong id"
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/' + str(did))
            if ispassed: 
                resobj = [
                {'key': 'id', 'type': 'int'},
                {'key': 'name', 'type': 'str'},
                {'key': 'email', 'type': 'str'},
                {'key': 'specialization', 'type': 'str'},
                {'key': 'weeklySchedule', 'type': 'list'}
            ]
                ispassed, msg = compare_objects(resobj, res)
            else:
                message += "<br> Endpoin failed: " + baseurl + suburl + '/' + str(did)
                message += "<br> Error in getting doctor by id"
        
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test5(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                    name=obj['name']
                    email=obj['email']
                    break
        if ispassed:
            payload = [
                {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': ''},
                {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
                {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
                {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                    {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                    {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                    {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                    {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/update/' + str(did), False)
            if ispassed:  message += "<br>Passed: Payload sent with blank name"
        
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':''},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/update/' + str(did), False)
            if ispassed: message += "<br>Passed: Payload sent with blank email"
            
        if ispassed:
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Tuesday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "10:00","endTime": "17:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "09:00","endTime": "16:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "10:00","endTime": "17:00"}]}    
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/update/' + str(did), False)
            if ispassed: message += "<br>Passed: Payload sent with blank specialization"
        
        
        
        if ispassed:
            
            payload = [
            {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': name},
            {'key': 'email', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':email},
            {'key': 'specialization', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': 'Pediatrician'},
            {'key': 'weeklySchedule', 'require': False, 'type': 'list', 'len': 10 , 'is_fixed': True, 'value':[{"dayOfWeek": "Monday","startTime": "08:00","endTime": "23:00"},
                                                                                                               {"dayOfWeek": "Tuesday","startTime": "08:00","endTime": "23:00"},
                                                                                                                {"dayOfWeek": "Wednesday","startTime": "08:00","endTime": "23:00"},
                                                                                                                {"dayOfWeek": "Thursday","startTime": "08:00","endTime": "23:00"},
                                                                                                                {"dayOfWeek": "Friday","startTime": "08:00","endTime": "23:00"}]}    
            ]

            resobj = [
                {'key': 'id', 'type': 'int'},
                {'key': 'name', 'type': 'str'},
                {'key': 'email', 'type': 'str'},
                {'key': 'specialization', 'type': 'str'},
                {'key': 'weeklySchedule', 'type': 'list'},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/update/' + str(did), True)
            if ispassed: 
                message += "<br>Passed: Payload sent with correct values"
                ispassed, msg = compare_objects(resobj, res)
            
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
            
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test6(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    msg=''
    try:
        ispassed, message, res = test1(baseurl)
        did=res['id']
        if ispassed:
            payload =[]
            ispassed, res, status_code = validate_post_request_custom(payload,baseurl + suburl + '/delete/1000' , False)
            if ispassed: 
                message += "<br> Passed: Record not  deleted with incorrect doctor id"
                
        if ispassed:
            payload =[]
            ispassed, res, status_code = validate_post_request_custom(payload,baseurl + suburl + '/delete/' + str(did) , True)
            if ispassed: 
                message += "<br> Passed: Record deleted "
                
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res 

def test7(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    current_date = datetime.now().date()
    daytoadd = generate_random_int_between_lengths(1) + 1
    # Add one day to the current date
    new_date = current_date + timedelta(days=daytoadd)
    past_date = current_date - timedelta(days=1)

    # Format the dates as strings in the desired format (YYYY-MM-DD)
    current_date_formatted = current_date.strftime('%Y-%m-%d')
    new_date_formatted = new_date.strftime('%Y-%m-%d')
    past_date_formatted = past_date.strftime('%Y-%m-%d')
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                    break
        if ispassed:
            payload = [
                {'key': 'leaveDate', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': str(new_date)},
                {'key': 'startTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':''},
                {'key': 'endTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' +'1000/leave', False)
            if ispassed:  message += "<br>Passed: Payload sent with wrong  doctorid"
        
        if ispassed:
            payload = [
                {'key': 'leaveDate', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': str(past_date_formatted)},
                {'key': 'startTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':''},
                {'key': 'endTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/leave', False)
            if ispassed:  message += "<br>Passed: Payload sent with past  leaveDate"
            
        if ispassed:
            payload = [
                {'key': 'leaveDate', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': str(new_date_formatted)},
                {'key': 'startTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':''},
                {'key': 'endTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': '12:00'},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/leave', False)
            if ispassed:  message += "<br>Passed: Payload sent with only endTime"
            
        if ispassed:
            payload = [
                {'key': 'leaveDate', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': str(new_date_formatted)},
                {'key': 'startTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':'09:00'},
                {'key': 'endTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': '08:00'},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/leave', False)
            if ispassed:  message += "<br>Passed: Payload sent with  endTime before starttime"
        
        
        
        
        if ispassed:
            
            payload = [
                {'key': 'leaveDate', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': str(new_date_formatted)},
                {'key': 'startTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value':'09:00'},
                {'key': 'endTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            ]

            resobj = [
                {'key': 'Doctor Id', 'type': 'int'},
                {'key': 'LeaveId', 'type': 'int'},
                {'key': 'LeaveDate', 'type': 'str'},
                {'key': 'StartTime', 'type': 'str'},
                {'key': 'EndTime', 'type': 'str'},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/leave', True)
            if ispassed: 
                message += "<br>Passed: Payload sent with correct values"
                ispassed, msg = compare_objects(resobj, res)
            
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
            
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test8(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    msg=''
    try:
        ispassed, message, res = test7(baseurl)
        DoctorId = res['Doctor Id']
        LeaveDate = res['LeaveDate']
        if ispassed:
            payload = []
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(DoctorId) +'/deleteLeave?leaveDate=2023-08-15', False)
            if ispassed:  message += "<br>Passed: Record Not deleted with wrong date"
        if ispassed:
            payload = []
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(DoctorId) +'/deleteLeave?leaveDate=' + str(LeaveDate), True)
            if ispassed:  message += "<br>Passed: Record deleted"
            
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
            
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res

def test9(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    current_date = datetime.now().date()
    random_number = random.randint(9, 18)
    formatted_number = '{:02}'.format(random_number)
    # Add one day to the current date
    new_date = current_date + timedelta(days=2)
   
    new_date_formatted = str(new_date.strftime('%Y-%m-%d')) + f'T{formatted_number}:30'

    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                    specialization=obj['specialization']
                    break
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/' + str(did) +'/availability?dateTime='+ '2023-07-27T15:30' )
            if ispassed == False:
                ispassed=True
                message += "<br> Passed: dateTime sent wrong"
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/' + str(did) +'/availability?dateTime='+ new_date_formatted )
            if ispassed: 
                message += "<br> Passed: Record fetched successfully"
            else:
                message += "<br> Endpoin failed: " + baseurl + suburl + '/' + str(did) +'/availability?dateTime='+ new_date_formatted
                message += "<br> Error in Fetching data"
        
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res        
def test10(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    current_date = datetime.now().date()
    random_number = random.randint(9, 18)
    formatted_number = '{:02}'.format(random_number)
    # Add one day to the current date
    new_date = current_date + timedelta(days=2)
    phone_number=random.randint(1000000000, 9999999999)
    new_date_formatted = str(new_date.strftime('%Y-%m-%d')) + f'T{formatted_number}:30'
    random_name=generate_random_string(10)

    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'}
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            message += "<br> Passed: Get all lists"
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                   
                    break
        if ispassed:
            payload = [
                {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': ''},
                {'key': 'contact', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': phone_number},
                {'key': 'dateTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': new_date_formatted},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/bookAppointments', False)
            if ispassed:  message += "<br>Passed: Payload sent with blank  name"
        if ispassed:
            payload = [
                {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': random_name},
                {'key': 'contact', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
                {'key': 'dateTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': new_date_formatted},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/bookAppointments', False)
            if ispassed:  message += "<br>Passed: Payload sent with blank  contact"
            
        if ispassed:
            payload = [
                {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': random_name},
                {'key': 'contact', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': phone_number},
                {'key': 'dateTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': ''},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/bookAppointments', False)
            if ispassed:  message += "<br>Passed: Payload sent with blank  dateTime"
        
        if ispassed:
            payload = [
                {'key': 'name', 'require': False, 'type': 'str', 'len': 10, 'is_fixed': True, 'value': random_name},
                {'key': 'contact', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': phone_number},
                {'key': 'dateTime', 'require': False, 'type': 'str', 'len': 10 , 'is_fixed': True, 'value': new_date_formatted},
            ]
            ispassed, msg, res = validate_post_request_custom(payload, baseurl + suburl + '/' + str(did) +'/bookAppointments', True)
            if ispassed:
                resobj = [
                        {'key': 'AppointmentId', 'type': 'int'},
                        {'key': 'DoctorId', 'type': 'int'},
                        {'key': 'DoctorName', 'type': 'str'},
                        {'key': 'DoctorEmail', 'type': 'str'},
                        {'key': 'DoctorSpecialization', 'type': 'str'},
                        {'key': 'PatientId', 'type': 'int'},
                        {'key': 'PatientName', 'type': 'str'},
                        {'key': 'PatientContact', 'type': 'str'},
                        {'key': 'dateTime', 'type': 'str'},
                    ]  
                message += "<br>Passed: Payload sent with Correct data"
                ispassed, msg = compare_objects(resobj, res)
        
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res      

def test11(baseurl):
    message = ""
    ispassed = False
    suburl = 'doctors'
    try:
        resobj = [
            {'key': 'id', 'type': 'int'},
            {'key': 'name', 'type': 'str'},
            {'key': 'email', 'type': 'str'},
            {'key': 'specialization', 'type': 'str'},
            
        ]
        ispassed, res, status_code = get_request(baseurl + suburl)
        if ispassed: 
            ispassed, msg = compare_objects(resobj, res)
            random.shuffle(res)
            did="0"
            for obj in res:
                if obj['id'] != '':
                    did = obj['id']
                   
                    break
        if ispassed:
            ispassed, res, status_code = get_request(baseurl + suburl + '/' +str(did) + '/appointments/upcoming')
            if ispassed ==False:
                message += "<br> Response: <br>" + str(res)
                message += "No data found or something issue in code please check the code and re run testcase" 
            if ispassed:
                resobj = [
                    {'key': 'doctorId', 'type': 'int'},
                    {'key': 'doctorName', 'type': 'str'},
                    {'key': 'specialization', 'type': 'str'},
                    {'key': 'upcomingAppointments', 'type': 'dict'},
                    
                ] 
                ispassed, msg = compare_objects(resobj, res)
        if ispassed == False:
            message += msg
        else:
            message += "<br> Response: <br>" + str(res)
    except Exception as e:
        message = str(e)
        ispassed = False
    return ispassed, message, res
            

if __name__ == "__main__":
    fileind = int(sys.argv[1])
    url = sys.argv[2]+'/'
    message = ''
    ispassed = False
    if site_check(url):
        try:
            if fileind == 1:
                ispassed, message, res = test1(url)
            elif fileind == 2:
                ispassed, message, res = test2(url)
            elif fileind == 3:
                ispassed, message, res = test3(url)
            elif fileind == 4:
                ispassed, message, res = test4(url)
            elif fileind == 5:
                ispassed, message, res = test5(url)
            elif fileind == 6:
                ispassed, message, res = test6(url)
            elif fileind == 7:
                ispassed, message, res = test7(url)
            elif fileind == 8:
                ispassed, message, res = test8(url)
            elif fileind == 9:
                ispassed, message, res = test9(url)
            elif fileind == 10:
                ispassed, message, res = test10(url)
            elif fileind == 11:
                ispassed, message, res = test11(url)
            else:
                print(f"Test case with fileind {fileind} not found.")

        except Exception as e:
            message += str(e)
            print("Error *** ", e)
            ispassed = False
    else:
        message = "<br>Your project is not running<br>Status code: 500"
        message += "<br>Run your project from Terminal Tab by clicking 'Run Project' Button"
    

    if ispassed:
        print("<br>------------- Test Case <b>Passed</b> -------------")
    else:
        print("<br>------------- Test Case <b>Failed</b> -------------")
    print(message)