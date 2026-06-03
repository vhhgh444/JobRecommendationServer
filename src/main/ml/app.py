from flask import Flask, request, jsonify
import pickle
import numpy as np

app = Flask(__name__)

clf=pickle.load(open("clf.pkl","rb"))
tfidf=pickle.load(open("tfidf.pkl","rb"))
encoder=pickle.load(open("encoder.pkl","rb"))

@app.route('/predict', methods=['POST'])
def predict():
    print("Reqest Recived")
    data = request.get_json()
    
    print(data)

    text = data.get("text", "")
    if not text:
        return jsonify({"error": "text is required"}), 400
    
    vector=tfidf.transform([text])
    dense_vector=vector.toarray()
    scores=clf.decision_function(dense_vector)[0]
    top3_idx=np.argsort(scores)[-3:] [::-1]
    top3_roles=[]
    prediction=clf.predict(dense_vector)
    
    role=encoder.inverse_transform(prediction)[0]
    
    for idx in top3_idx:
        top3_roles.append({
            "role":encoder.classes_[idx],
            "score":round(float(scores[idx]),4)
        })

    # Dummy prediction
    print("TOP 3 ROLES =", top3_roles)
    return jsonify({
        "role": role,
        "recommendedRoles":top3_roles,
        "skills": [],
        "version":"NEW_CODE"
    })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)