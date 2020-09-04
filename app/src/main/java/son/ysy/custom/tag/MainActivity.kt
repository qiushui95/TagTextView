package son.ysy.custom.tag

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var currentPosition: TagTextView.Position = TagTextView.Position.TopStart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tagView.setOnClickListener {
            val nextPosition = when (currentPosition) {
                TagTextView.Position.TopStart -> {
                    TagTextView.Position.TopEnd
                }
                TagTextView.Position.TopEnd -> {
                    TagTextView.Position.BottomEnd
                }
                TagTextView.Position.BottomEnd -> {
                    TagTextView.Position.BottomStart
                }
                TagTextView.Position.BottomStart -> {
                    TagTextView.Position.TopStart
                }
            }
            tagView.setPosition(nextPosition)
                .rebuild()
            currentPosition = nextPosition
        }
    }
}