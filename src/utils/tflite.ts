import Tflite from 'react-native-tflite';

let tflite: any = null;

export const loadModel = async () => {
    return new Promise((resolve, reject) => {
        tflite = new Tflite();
        tflite.loadModel(
            {
                model: 'models/wheel-detector.tflite', // path จาก assets
                labels: 'models/labels.txt',           // ถ้ามี label file
            },
            (err: any, res: any) => {
                if (err) reject(err);
                else resolve(res);
            }
        );
    });
};

export const runDetection = async (image: string) => {
    return new Promise((resolve, reject) => {
        if (!tflite) return reject('Model not loaded');
        tflite.detectObjectOnImage(
            {
                path: image,            // image path หรือ base64
                threshold: 0.5,
                numResultsPerClass: 5,
            },
            (err: any, res: any) => {
                if (err) reject(err);
                else resolve(res);
            }
        );
    });
};
