# Chameleon-Seekbar
A Custom seekbar for android.

<img src = "https://github.com/OmkarKadam01/Chameleon-Seekbar/blob/master/Art/seekbar.gif" />



## Usage

 - XML
 ```
 <com.colorseekbar.ColorSeekBar
        android:id="@+id/color_seek_bar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />
```

- Attributes

<table>
 <th>Attribute Name</th>
 <th>Description</th>
 <tr>
    <td>app:colorSeeds="@array/colors"</td>
    <td>Set the colors of seekbar</td>
 </tr>
  <tr>
    <td>app:cornerRadius="4dp"</td>
    <td>Sets the corner radius of bar.</td>
 </tr> 
 <tr>
    <td>app:barHeight="8dp</td>
    <td>Sets the height of color bar.</td>
 </tr>
  <tr>
    <td>app:thumbBorder="4dp"</td>
    <td>Sets the width of thumb border.</td>
 </tr>
  <tr>
    <td>app:thumbBorderColor="@android:color/black"</td>
    <td>sets the color of thumb border.</td>
 </tr>
 </table>
 
 - Listener
 
 ```
 color_seek_bar.setOnColorChangeListener(object: ColorSeekBar.OnColorChangeListener{
            override fun onColorChangeListener(color: Int) {
                //gives the selected color
                view.setBackgroundColor(color)
            }
        })
```


Its extended version of color picker library by Divyanshu

URL : https://github.com/divyanshub024/ColorSeekBar

