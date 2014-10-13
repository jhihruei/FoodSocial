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


@app.route('/api/testWall',methods=['POST'])
def apikey():
    from post import getPostWall
    data = request.get_json(force=True)
    #result = followUser(data['userID'],data['followUser'])
    result = getPostWall(data['userID'])
    return jsonify({"stat":result})

@app.route('/api/post',methods=['POST'])#FS-001 發佈API
def post():
    from post import post_to_mongo
    data = request.get_json(force=True)
    result = post_to_mongo(data['poster'],data['recommendBy'],data['title'],data['address'],data['latitude'],data['content'],data['picture'])
    return jsonify({"stat":1,"id":str(result)})

@app.route('/api/addUser',methods=['POST'])#FS-002 新增user
def addUser():
    from user import adduser
    udata = request.get_json(force=True)
    adduser(udata['account'],udata['password'],udata['accountName'],udata['fbID'],udata['loginDevice'])
    return jsonify({"stat":1})

@app.route('/api/login',methods=['POST'])#FS-003 登入
def login():
    from user import login
    ldata = request.get_json(force=True)
    result = login(ldata['type'],ldata['account'],ldata['password'],ldata['fbID'])
    return jsonify({"stat":result})

@app.route('/api/followUser',methods=['POST'])#FS-004 follow 其他user
def follow():
    from user import followUser
    data = request.get_json(force=True)
    result = followUser(data['userID'],data['followUser'])
    return jsonify({"stat":result})

@app.route('/api/getUserPostWall',methods=['POST'])#FS-005 個人PostWall
def getUserPostWall():
    from post import getUserPostWall
    data = request.get_json(force=True)
    result = getUserPostWall(data['userID'])
    return jsonify({"stat":1,"result":result})


if __name__ == '__main__':
    app.run(debug=True)