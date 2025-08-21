import React, {useEffect, useState, useMemo} from 'react';
import {
  View,
  StyleSheet,
  PermissionsAndroid,
  Platform,
  useWindowDimensions,
} from 'react-native';
import {
  Camera,
  useCameraDevices,
  useFrameProcessor,
} from 'react-native-vision-camera';
import {runOnJS} from 'react-native-reanimated';
import type {Detection} from './types';
import BoundingBox from './components/BoundingBox';

export default function App() {
  const devices = useCameraDevices();
  const device = devices.back; // หรือ devices.front ตามต้องการ
  const {width: viewW, height: viewH} = useWindowDimensions();

  const [authorized, setAuthorized] = useState(false);
  const [detections, setDetections] = useState<Detection[]>([]);
  const [frameSize, setFrameSize] = useState<{w: number; h: number}>({
    w: 1280,
    h: 720,
  }); // จะอัปเดตจาก frame จริง

  useEffect(() => {
    (async () => {
      const cam = await Camera.requestCameraPermission();
      if (cam !== 'granted') return;
      if (Platform.OS === 'android') {
        await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
      }
      setAuthorized(true);
    })();
  }, []);

  // รับผลจาก native (Array<{x,y,w,h,score,cls}>) — ค่าพิกัดเป็นหน่วย “input-space” ของโมเดล (640x640 center-format)
  const onDetections = (arr: any[]) => {
    // ค่า x,y,w,h มาจาก YOLO แบบ center (cx,cy,w,h) บนสเกล input=640
    // แปลงเป็น absolute pixel ของ frame preview
    const INPUT = 640; // ต้องตรงกับ InterpreterManager
    const scaleX = frameSize.w / INPUT;
    const scaleY = frameSize.h / INPUT;

    const mapped: Detection[] = arr.map(d => ({
      x: d.x,
      y: d.y,
      w: d.w,
      h: d.h,
      score: d.score,
      cls: d.cls,
    }));
    setDetections(
      mapped.map(d => ({
        ...d,
        x: d.x * scaleX,
        y: d.y * scaleY,
        w: d.w * scaleX,
        h: d.h * scaleY,
      })),
    );
  };

  // คำนวณ scale จากขนาด frame → ขนาด view เพื่อวาง overlay ตรง
  const {scaleX: viewScaleX, scaleY: viewScaleY} = useMemo(() => {
    // สมมุติเราถ่ายที่ 1280x720 (16:9) และ preview fill = cover
    // เพื่อความง่าย: ใช้ letterbox-fit โดยคงอัตราส่วน
    const ratioFrame = frameSize.w / frameSize.h;
    const ratioView = viewW / viewH;

    if (ratioView > ratioFrame) {
      // view กว้างกว่า ⇒ fit ตามความสูง
      const scaledW = ratioFrame * viewH;
      const offsetX = (viewW - scaledW) / 2;
      return {
        scaleX: scaledW / frameSize.w,
        scaleY: viewH / frameSize.h,
        offsetX,
        offsetY: 0,
      };
    } else {
      // view แคบกว่า ⇒ fit ตามความกว้าง
      const scaledH = viewW / ratioFrame;
      const offsetY = (viewH - scaledH) / 2;
      return {
        scaleX: viewW / frameSize.w,
        scaleY: scaledH / frameSize.h,
        offsetX: 0,
        offsetY,
      };
    }
  }, [frameSize, viewW, viewH]) as any;

  const frameProcessor = useFrameProcessor(frame => {
    'worklet';
    // เก็บขนาด frame (ครั้งแรก ๆ)
    // @ts-ignore VisionCamera v3 ให้เข้าถึง width/height
    // หากไม่มี ให้ส่งผ่าน params ใน native แล้ว return มาด้วย
    // @ts-ignore
    const fw = frame.width;
    const fh = frame.height;

    // เรียกปลั๊กอินที่เราลงทะเบียนไว้ใน MainApplication
    // detectWheels() จะ return Array<map>
    // @ts-ignore
    const result = frame.detectWheels();

    if (fw && fh) {
      // ส่งขึ้น JS แค่ครั้งแรก หรือจะส่งทุกครั้งก็ได้ แต่เราจะกรองใน JS
      // @ts-ignore
      runOnJS(setFrameSize)({w: fw, h: fh});
    }
    if (result) {
      // @ts-ignore
      runOnJS(onDetections)(result);
    }
  }, []);

  if (!device || !authorized)
    return <View style={{flex: 1, backgroundColor: '#000'}} />;

  return (
    <View style={styles.container}>
      <Camera
        style={StyleSheet.absoluteFill}
        device={device}
        isActive={true}
        frameProcessor={frameProcessor}
        fps={8} // S23U ทำได้สูงกว่านี้ ลองปรับได้
        photo={false}
        video={false}
      />
      {/* วาดกรอบ – แปลงพิกัดให้ลงบน View */}
      <View style={StyleSheet.absoluteFill}>
        {detections.map((d, i) => {
          // แปลงเป็น corner (top-left)
          const left = (d.x - d.w / 2) * viewScaleX;
          const top = (d.y - d.h / 2) * viewScaleY;
          const width = d.w * viewScaleX;
          const height = d.h * viewScaleY;
          return (
            <BoundingBox
              key={i}
              left={left}
              top={top}
              width={width}
              height={height}
              label={'wheel'}
              score={d.score}
            />
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {flex: 1, backgroundColor: '#000'},
});
