import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

type Props = {
  left: number;
  top: number;
  width: number;
  height: number;
  label?: string;
  score?: number;
};

export default function BoundingBox({
  left,
  top,
  width,
  height,
  label = 'wheel',
  score,
}: Props) {
  return (
    <View style={[styles.box, {left, top, width, height}]}>
      <Text style={styles.tag}>
        {label}
        {score != null ? ` ${(score * 100).toFixed(0)}%` : ''}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  box: {
    position: 'absolute',
    borderWidth: 2,
    borderColor: 'red',
    zIndex: 10,
  },
  tag: {
    position: 'absolute',
    left: 0,
    top: -18,
    backgroundColor: 'rgba(255,0,0,0.7)',
    color: '#fff',
    fontSize: 12,
    paddingHorizontal: 4,
    borderTopLeftRadius: 2,
    borderTopRightRadius: 2,
  },
});
