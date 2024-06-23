import os
import flask_bcrypt as bcrypt
import binascii

def hash_password(password):
    # 1. Generate salt
    salt = os.urandom(32)
    salt = (binascii.hexlify(salt)).decode('utf-8')
    # 2. Append salt to the password
    password = password + salt
    # 3. Hash the password and storing
    password = bcrypt.generate_password_hash(password).decode('utf-8')

    return password, salt

# def verify_password(user_mail, inputted_password):
#     # 1. Retrive user's salt from the database
#     authentication_data = database_helper.get_users_salt(email)
#     # 2. Append salt from DB
#     inputed_password = inputed_password + authentication_data['salt']
#     # 3. Compare the hash generated from the inputed password with the one in
#     #    the database
#     return bcrypt.check_password_hash(authentication_data['password'], inputed_password)