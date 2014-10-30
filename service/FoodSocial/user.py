#-*- coding: UTF-8 -*- 
from pymongo import MongoClient
from datetime import datetime
from flask import  jsonify,json

def adduser(account,password,accountName,fbID,loginDevice): #新增user
    from apiKey import createApiKey
    mc = MongoClient()
    userCon = mc.foodSocial
    userCol = userCon.users
    curDate = datetime.now()
    newUserID = getUserNextSeq()
    userData = {"userID":newUserID,"account":account,"password":password,"accountName":accountName,"fbID":fbID,"apiKey":createApiKey(accountName+curDate.strftime('%Y-%m-%d %H:%M:%S')),"followUser":[newUserID],"lastLoginTime":curDate,"createdTime":curDate,"loginDevice":loginDevice}
    userCheck = userCol.insert(userData)
    mc.close()
    return userCheck

def getUserNextSeq(): #回傳下一個users collection的userID index
    umc = MongoClient()
    seqCon = umc.foodSocial.users
    seqjson = seqCon.find_and_modify(query={"_id":"userID"}, update={'$inc': {'seq':1}},upsert=False, full_response=True)
    umc.close()
    return seqjson['value']['seq'] + 1

def login(type,account,password,fbID): #登入api
    mc = MongoClient()
    lCon = mc.foodSocial
    lCol = lCon.users
    if type == 'account':
        checkLogin = lCol.find_one({"account":account},{"password":1,"loginDevice":1,"userID":1})
        if checkLogin['password'] == password:
            mc.close()
            updateLoginTime(checkLogin['userID'])
            #return 1
            return jsonify({"stat":1,"userID":int(checkLogin['userID'])})
        else:
            mc.close()
            return 0
    elif type == 'fb':
        mc.close()
        return 0

def updateLoginTime(userid): #傳入userid更新最後登入時間
    mc = MongoClient()
    uCon = mc.foodSocial
    uCol = uCon.users
    result = uCol.update({"userID":userid},{'$set':{"lastLoginTime":datetime.now()}})
    mc.close()
    return result

def checkAccount():
        pass    

def followUser(userid,followid):#follow 其他User
    mc = MongoClient()
    uCon = mc.foodSocial
    uCol = uCon.users
    result = uCol.update({"userID":userid},{'$push':{"followUser":followid}})
    mc.close()
    return result

def getUserName(userid):
    mc = MongoClient()
    uCol = mc.foodSocial.users
    result = uCol.find_one({"userID":userid},{"accountName":1})
    mc.close()
    return result['accountName']