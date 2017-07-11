'use strict';

import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  Image,
  TouchableOpacity,
  TouchableWithoutFeedback,
  ToastAndroid,
  View
} from 'react-native';

class HelloWorld extends React.Component {

constructor(props){
    super(props);
    this.state ={
        target:props.target,
        index:0
    };
}

logoClick(){
    var i = this.state.index;
    i++;
    this.setState({index:i});
    ToastAndroid.show(i,ToastAndroid.SHORT);
}

  render() {
    return (
      <View style={styles.container}>
      <TouchableWithoutFeedback onLongPress={this.logoClick()} onPress={()=>ToastAndroid.show(this.state.index)}>
        <Image source={{uri:'logo'}} style={{height:100,width:100}} />
      </TouchableWithoutFeedback>
        <Text style={styles.appName}>路和远方</Text>
        <Text style={styles.inc}>福州云之智网络科技有限出品</Text>
      </View>
    )
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems:'center'
  },
  appName: {
    fontSize: 20,
    textAlign: 'center',
    margin: 40,
  },
  inc: {
      fontSize: 16,
      textAlign: 'center',
      margin: 20,
    },
});

AppRegistry.registerComponent('HelloWorld', () => HelloWorld);