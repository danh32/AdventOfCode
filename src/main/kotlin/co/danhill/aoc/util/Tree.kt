package co.danhill.aoc.util

class TreeNode<T>(val data: T) {
    private val _children = mutableListOf<TreeNode<T>>()
    val children: List<TreeNode<T>>
        get() = _children

    lateinit var parent: TreeNode<T>

    fun addChild(child: TreeNode<T>) {
        _children += child
    }

    fun asSequence(): Sequence<TreeNode<T>> {
        return sequence {
            yield(this@TreeNode)
            children.forEach { child -> yieldAll(child.asSequence()) }
        }
    }
}