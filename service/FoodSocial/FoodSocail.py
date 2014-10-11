#!flask/bin/python
#-*- coding: UTF-8 -*- 
from flask import Flask, request, jsonify,json


app = Flask(__name__)

@app.route('/')
def index():
    info = {'project name': 'FoodSocial', 'API': 'v.1.0'}
    return jsonify(info)

@app.route('/test/', methods=['GET'])
def test():
    inputA = request.args.get('A',0)
    inputB = request.args.get('B',0)
    test = {'user':'jack','A':inputA,'B':inputB}
    return jsonify(test)

@app.route('/mongo/',methods=['GET'])
def mongo():
    from pymongo import MongoClient
    con = MongoClient()
    db = con.foodSocial
    test = db.users
    out = test.find_one({"account":"megshao"},{"password":1,"loginDevice":1})
    con.close()
    return jsonify({"password":out['password'],"device":out['loginDevice']})


@app.route('/api/testFollow',methods=['POST'])
def apikey():
    from user import followUser
    data = request.get_json(force=True)
    result = followUser(data['userID'],data['followUser'])
    return jsonify({"stat":result})

@app.route('/api/post',methods=['POST'])#API-001 發佈API
def post():
    from post import post_to_mongo
    data = request.get_json(force=True)
    result = post_to_mongo(data['poster'],data['recommendBy'],data['title'],data['address'],data['latitude'],data['content'],data['picture'])
    return jsonify({"stat":1,"id":str(result)})

@app.route('/api/addUser',methods=['POST'])#API-002 新增user
def addUser():
    from user import adduser
    udata = request.get_json(force=True)
    adduser(udata['account'],udata['password'],udata['accountName'],udata['fbID'],udata['loginDevice'])
    return jsonify({"stat":1})

@app.route('/api/login',methods=['POST'])#API-003 登入
def login():
    from user import login
    ldata = request.get_json(force=True)
    result = login(ldata['type'],ldata['account'],ldata['password'],ldata['fbID'])
    return jsonify({"stat":result})

@app.route('/api/followUser',methods=['POST'])#API-004 follow 其他user
def follow():
    from user import followUser
    data = request.get_json(force=True)
    result = followUser(data['userID'],data['followUser'])
    return jsonify({"stat":result})


if __name__ == '__main__':
    app.run(debug=True)