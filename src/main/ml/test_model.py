@app.route('/predict', methods=['POST'])
def predict():
    return jsonify({
        "test": "I AM THE NEW FLASK SERVER"
    })