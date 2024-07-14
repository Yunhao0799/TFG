import os
import flask_bcrypt as bcrypt
import binascii

from users.models import CustomUser


def hash_password(password):
    # 1. Generate salt
    salt = os.urandom(32)
    salt = (binascii.hexlify(salt)).decode('utf-8')
    # 2. Append salt to the password
    password = password + salt
    # 3. Hash the password and storing
    password = bcrypt.generate_password_hash(password).decode('utf-8')

    return password, salt


def verify_password(user, inputted_password):
    # 1. Retrive user's salt from the database
    salt = user.salt

    # 2. Append salt from DB
    inputted_password = inputted_password + salt

    # 3. Compare the hash generated from the inputted password with the one in
    #    the database
    return bcrypt.check_password_hash(user.password, inputted_password)
