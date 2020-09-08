# TagTextView  
## 角标控件  [![](https://jitpack.io/v/qiushui95/TagTextView.svg)](https://jitpack.io/#qiushui95/TagTextView)

``` project build.gradle
allprojects {
    repositories {
        maven{url "https://jitpack.io"}
    }
}
```

```
dependencies {
    implementation("com.github.qiushui95:TagTextView:1.0.0")
}
```

### 预览  

| 左上 | 右上 |右下|左下|
|:-:|:-:|:-:|:-:|
| ![](https://raw.githubusercontent.com/qiushui95/TagTextView/master/images/top_start.png) | ![](https://raw.githubusercontent.com/qiushui95/TagTextView/master/images/top_end.png) |![](https://raw.githubusercontent.com/qiushui95/TagTextView/master/images/bottom_end.png)|![](https://raw.githubusercontent.com/qiushui95/TagTextView/master/images/bottom_start.png)|

### 属性

| 序号 |     attrs属性     |       方法       |                             类型                             |  默认值  |                说明                 |
| :--: | :---------------: | :--------------: | :----------------------------------------------------------: | :------: | :---------------------------------: |
|  1   | ttv_tagSizeRatio  | setTagSizeRatio  |                          float(0,1)                          |   0.35   |        设置角标宽度:控件的宽        |
|  2   | ttv_tagStartRatio | setTagStartRatio |                          float[0,1)                          |    0     |        设置角标起始位置比例         |
|  3   |   ttv_tagColor    |   setTagColor    |                           ColorInt                           |   RED    |            设置角标颜色             |
|  4   |  ttv_tagPosition  |   setPosition    | enum:<br />TopStart(左上)<br />TopEnd(右上)<br />BottomEnd(右下)<br />BottomStart(左下) | TopStart |            设置角标位置             |
|  5   |   ttv_textRatio   | setTextSizeRatio |                          float(0,1)                          |   0.6    |    设置文字占角标比例,范围(0,1)     |
|  6   |  ttv_textRotate   |  setTextRotate   |                            float                             |    0     | 设置文字旋转角度,旋转中心为角标中心 |
|  7   |     ttv_text      |     setText      |                            String                            |          |              设置文字               |
|  8   |   ttv_textColor   |   setTextColor   |                           ColorInt                           |  WHITE   |            设置字体颜色             |

